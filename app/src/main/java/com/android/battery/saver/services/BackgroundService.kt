package com.android.battery.saver.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.android.battery.saver.dao.UsageInfoDAOImpl
import com.android.battery.saver.helper.ReadWriteFile
import com.android.battery.saver.logger.Logger
import com.android.battery.saver.managers.AppManager
import com.android.battery.saver.managers.CpuManager
import com.android.battery.saver.model.UsageInfoModel
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.SECONDS


class BackgroundService : Service() {
    //Objects
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private lateinit var usageInfoDAO: UsageInfoDAOImpl

    //Variable
    private var isScreenOn = true
    private val excludedApps = createExcludedAppsSet()
    private var appConfiguration = UsageInfoModel(
            "",
            0, arrayListOf(), arrayListOf()
    )
    private var userComplained = false
    private var clock = 0
    private val timeInterval = 2
    private val refreshInterval: Long = 2

    override fun onCreate() {
        super.onCreate()
        usageInfoDAO = UsageInfoDAOImpl(applicationContext)
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction("com.android.battery.saver.USER_COMPLAINED")
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(broadcastRcv, filter)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        var currentApp: String
        var lastApp = ""

        val runnable = Runnable {
            //Check if screen is on or off
            if (isScreenOn) {
                //Get THE PACKAGE NAME of the app that is running in foreground
                currentApp = AppManager.getTopApp()
                if (!excludedApps.contains(currentApp)) {
                    //The user could be using the same app for many seconds/minutes
                    if (lastApp != currentApp) {
                        if ((userComplained || checkIfConfigurationHasChanged(appConfiguration)) && lastApp != "") {
                            //TODO
                            //Need service to change birghtness level or keep it as automatic by the system?
                            val usageInfoModel = UsageInfoModel(currentApp, 100,
                                    CpuManager.getAllCoresFrequencies(),
                                    CpuManager.getAllCoresThreshold())
                            //save appConfiguration config
                            if (appConfiguration.appName == "") {
                                usageInfoDAO.insert(usageInfoModel)
                            }
                            //Update config
                            else {
                                usageInfoDAO.update(usageInfoModel)
                            }
                        }
                        appConfiguration = usageInfoDAO.getUsageInfoByAppName(currentApp)
                        //The app with this package name does not exist in the DB
                        //THIS IS A TOTALLY NEW APP THAT WAS NOT EXECUTED BEFORE
                        if (appConfiguration.appName == "") {
                            //Scale cpu to max
                            CpuManager.setAllCoresToMax()
//                            Log.d(Logger.DEBUG, "Scaled cpu to max for app %s" + currentApp)
                        }
                        lastApp = currentApp
                        clock = 2
                    } else {
                        //TODO
                        //Setar novo threshold quando ja exisita threshold e random deu <9 e usuario n reclamou
                        clock++
                        if (clock % timeInterval == 0) {
                            CpuManager.scaleCpuDown()
                        }
                    }
                }
            } else {
                //Need to save the state of the last running app if it changed
                //Set isScreenOn to off
                isScreenOn = false
                //Set the cpu to min possible
//                cpuManager.setToMinSpeed()
            }
        }
        scheduler.scheduleAtFixedRate(runnable, 1, refreshInterval, SECONDS)
        return START_NOT_STICKY
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
        //TODO
        //Checks if brightness changed
        //Checks if CPU changed
        for (i in 0 until CpuManager.getNumberOfCores()) {
            if (CpuManager.getFrequencyFromCore(i) != app.coreFrequencies[0])
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
                CpuManager.setThreshold()
                CpuManager.rampCpuUp()
            }
        }
    }
}