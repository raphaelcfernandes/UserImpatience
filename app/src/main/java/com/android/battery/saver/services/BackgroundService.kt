package com.android.battery.saver.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.android.battery.saver.dao.UsageInfoDAOImpl
import com.android.battery.saver.helper.FractionParser
import com.android.battery.saver.helper.Preferences
import com.android.battery.saver.logger.Logger
import com.android.battery.saver.managers.AppManager
import com.android.battery.saver.managers.CpuManager
import com.android.battery.saver.model.UsageInfoModel
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.MILLISECONDS


class BackgroundService : Service() {
    //Objects
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private lateinit var usageInfoDAO: UsageInfoDAOImpl
    private lateinit var runnable: Runnable

    //Variable
    private var needsToReload = false
    private var isScreenOn = true
    private val excludedApps = createExcludedAppsSet()
    private var appConfiguration = UsageInfoModel(
            "",
            0, arrayListOf(), arrayListOf()
    )
    private var userComplained = false
    private var clock = 0L
    private var decreaseCPUInterval = 0
    private var readTAinterval: Long = 0
    private var amountOfFrequencyToReduce = 0
    private var increaseCpuAfterComplaining = 0.0
    private lateinit var governor: String

    override fun onCreate() {
        super.onCreate()
        usageInfoDAO = UsageInfoDAOImpl(applicationContext)
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction("com.android.battery.saver.USER_COMPLAINED")
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(broadcastRcv, filter)
        governor = Preferences.getGovernor(applicationContext)!!
        if (governor == "UImpatience") {
            readTAinterval = Preferences.getReadTAInterval(applicationContext!!)!!.toLong()
            decreaseCPUInterval = Preferences.getDecreaseCPUInterval(applicationContext!!)!!
            amountOfFrequencyToReduce = Preferences.getDecreaseCPUFrequency(applicationContext!!)
            increaseCpuAfterComplaining = FractionParser.parse(Preferences.getMarginToIncreaseCpu(applicationContext!!)!!)
        }

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        var currentApp: String
        var lastApp = ""
        runnable = Runnable {
            //Check if screen is on or off
            if (isScreenOn) {
                if (userComplained) {
                    CpuManager.setThreshold()
                    CpuManager.rampCpuUp(increaseCpuAfterComplaining)
                    userComplained = false
                    clock = 0
                }
                //Get THE PACKAGE NAME of the app that is running as top-app
                currentApp = AppManager.getTopApp()
                if (!excludedApps.contains(currentApp)) {
                    //The user could be using the same app for many seconds/minutes
                    if (lastApp != currentApp || needsToReload) {
                        needsToReload = false
                        if (checkLastAppState(lastApp))
                            saveLastAppState(lastApp)
                        appConfiguration = usageInfoDAO.getDataFromAppByName(currentApp)
                        //The app with this package name does not exist in the DB
                        //THIS IS A TOTALLY NEW APP THAT WAS NOT EXECUTED BEFORE
                        if (appConfiguration.appName == "") {
                            //Scale cpu to max
                            CpuManager.setAllCoresToMax()
                        } else {
                            //Scale cpu to the specific app
                            CpuManager.scaleCpuToApp(
                                    appConfiguration.coreFrequencies,
                                    appConfiguration.coreThresholds
                            )
                        }
                        lastApp = currentApp
                        clock = 0
                    } else {
//                        //TODO
//                        //Setar novo threshold quando ja exisita threshold e random deu <9 e usuario n reclamou
                        clock += readTAinterval
                        if (clock >= decreaseCPUInterval) {
                            CpuManager.scaleCpuDown(amountOfFrequencyToReduce)
                            clock = 0
                        }
                    }
                }
            } else {
                //Set cpu to min just once
                if (!needsToReload) {
                    //Need to save the state of the last running app if it changed
                    if (checkLastAppState(lastApp))
                        saveLastAppState(lastApp)
                    //Set isScreenOn to off
                    isScreenOn = false
                    //Set the cpu to min possible
                    CpuManager.setToMinSpeed()
                    needsToReload = true
                    lastApp = ""
                }
            }
        }

        scheduler.scheduleAtFixedRate(runnable, 1000, readTAinterval, MILLISECONDS)
        return START_NOT_STICKY
    }

    private fun checkLastAppState(lastApp: String): Boolean {
        if (checkIfConfigurationHasChanged(appConfiguration) && lastApp != "") {
            return true
        }
        return false
    }

    private fun saveLastAppState(lastApp: String) {
        //TODO
        //Need service to change brightness level or keep it as automatic by the system?
        val usageInfoModel = UsageInfoModel(lastApp, 100,
                CpuManager.getAllCoresFrequencies(),
                CpuManager.getAllCoresThreshold())
        //save appConfiguration config
        if (appConfiguration.appName == "") {
            usageInfoDAO.insert(usageInfoModel)
        }
        //Update last config
        else {
            usageInfoDAO.update(usageInfoModel)
        }
    }

    private fun createExcludedAppsSet(): HashSet<String> {
        val excludedAppsArrayList = arrayOf(
                "com.android.launcher",
                "com.google.android.googlequicksearchbox",
                "com.android.systemui",
                "android"
        )
        return excludedAppsArrayList.toHashSet()
    }

    private fun checkIfConfigurationHasChanged(app: UsageInfoModel): Boolean {
        //This a totally new app, so we need to save it. Don't need to check for changes
        if (app.appName == "") {
            return true
        }
        for (i in 0 until CpuManager.getNumberOfCores()) {
            if (CpuManager.getFrequencyFromCore(i) != app.coreFrequencies[i])
                return true
        }
        return false
    }

    private val broadcastRcv = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                isScreenOn = false
                Log.d(Logger.DEBUG, "SCREEN OFF")
            }
            if (intent.action == Intent.ACTION_SCREEN_ON) {
                isScreenOn = true
                Log.d(Logger.DEBUG, "SCREEN ON")
            }
            if (intent.action == "com.android.battery.saver.USER_COMPLAINED") {
                Log.d(Logger.DEBUG, "USER COMPLAINED")
                userComplained = true

            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastRcv)
        scheduler.shutdown()
        super.onDestroy()
    }
}