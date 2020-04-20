package com.android.battery.saver.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.android.battery.saver.dao.UsageInfoDAOImpl
import com.android.battery.saver.event.OnCpuResetThreshold
import com.android.battery.saver.event.OnCpuScaleAllDownEvent
import com.android.battery.saver.event.OnCpuScaleAllUpEvent
import com.android.battery.saver.event.OnSetAllCoreToMaxEvent
import com.android.battery.saver.helper.Preferences
import com.android.battery.saver.logger.Logger
import com.android.battery.saver.managers.AppManager
import com.android.battery.saver.managers.CpuManager
import com.android.battery.saver.model.UsageInfoModel
import org.greenrobot.eventbus.EventBus
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.SECONDS

class BackgroundService : Service() {
    //Objects
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private lateinit var usageInfoDAOImpl: UsageInfoDAOImpl
    private lateinit var runnable: Runnable

    //Variable
    private var needsToReload = false
    private var isScreenOn = true
    private val excludedApps = createExcludedAppsSet()
    private var appConfiguration = UsageInfoModel(
            "",
            0, arrayListOf(), arrayListOf()
    )
    private var clock = 0
    private var decreaseCPUInterval = 0
    private var readTAinterval: Int = 0
    private var decreaseCPUFrequency = 0
    private var increaseCpuAfterComplaining = 0
    private var uimpatienceLevel = 0
    private lateinit var currentApp: String
    private var lastApp = ""
    private var iteration = 0

    override fun onCreate() {
        super.onCreate()
        usageInfoDAOImpl = UsageInfoDAOImpl(this)
        readTAinterval = Preferences.getReadTAInterval(this)!!
        decreaseCPUInterval = Preferences.getDecreaseCPUInterval(this)!!
        decreaseCPUFrequency = Preferences.getDecreaseCPUFrequency(this)
        increaseCpuAfterComplaining = Preferences.getMarginToIncreaseCpu(this)!!
        uimpatienceLevel = Preferences.getImpatienceLevel(this)!!
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
        var incIteration = false
//        readTAinterval = 1
//        decreaseCPUInterval = 2
        runnable = Runnable {
            //Check if screen is on or off
            if (isScreenOn) {
                //Get THE PACKAGE NAME of the app that is running as top-app
                currentApp = AppManager.getTopApp()
                if (!excludedApps.contains(currentApp)) {
                    //The user could be using the same app for many seconds/minutes
                    // needsToReload tells the governor to reload the frequency if the screen turns off
                    // FOR TESTING PURPOSES. THIS LINE VERIFIES IF THE TEST THAT WAS RUNNING HAS FINISHED
                    // 30/11/2019
                    if (lastApp != currentApp) {
                        // using this line to execute tests to collect data/results
                        if (currentApp == "com.google.android.googlequicksearchbox" || currentApp == "com.google.android.googlequicksearchbox:search") {
                            incIteration = true
                            EventBus.getDefault().post(OnCpuResetThreshold())
                        } else {
                            if (incIteration) {
                                Log.d(Logger.DEBUG, "adding to iteration: $iteration")
                                iteration++
                                incIteration = false
                                println("Setting core to max")
                                EventBus.getDefault().post(OnSetAllCoreToMaxEvent())
                                clock = 0
                            }
                        }
                        lastApp = currentApp
                    } else if (lastApp == currentApp && (currentApp != "com.google.android.googlequicksearchbox" && currentApp != "com.google.android.googlequicksearchbox:search")) {
                        clock += readTAinterval
                        // decrease cpu and save to db:
                        // cpu freqs, iteration, timestamp, app, readTa, decreaseCpuInterval, decreaseCpuFreq, IncreaseCpuFreq, uimpatiencelevel
                        if (clock >= decreaseCPUInterval) {
                            EventBus.getDefault().post(OnCpuScaleAllDownEvent(currentApp, readTAinterval,
                                    iteration - 1, decreaseCPUInterval, decreaseCPUFrequency,
                                    increaseCpuAfterComplaining, uimpatienceLevel))
                            clock = 0
                        }
                    }
                }
            }
        }

        scheduler.scheduleAtFixedRate(runnable, 0, readTAinterval.toLong(), SECONDS)
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
            usageInfoDAOImpl.insert(usageInfoModel)
        }
        //Update last config
        else {
            usageInfoDAOImpl.update(usageInfoModel)
        }
    }

    private fun createExcludedAppsSet(): HashSet<String> {
        val excludedAppsArrayList = arrayOf(
                "com.android.launcher",
                "com.android.systemui",
                "com.android.battery.saver",
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
            }
            if (intent.action == Intent.ACTION_SCREEN_ON) {
                isScreenOn = true
            }
            if (intent.action == "com.android.battery.saver.USER_COMPLAINED") {
                println("Increasing CPU freq")
                EventBus.getDefault().post(OnCpuScaleAllUpEvent(increaseCpuAfterComplaining, currentApp,
                        readTAinterval, iteration - 1, decreaseCPUInterval, decreaseCPUFrequency,
                        increaseCpuAfterComplaining, uimpatienceLevel))
                clock = 0
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastRcv)
        scheduler.shutdown()
        CpuManager.stop()
        super.onDestroy()
    }
}