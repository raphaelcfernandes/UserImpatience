package com.android.battery.saver.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.android.battery.saver.database.AppDbHelper
import com.android.battery.saver.managers.AppManager
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.SECONDS


class BackgroundService : Service() {
    //Objects
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private lateinit var appDbHelper: AppDbHelper

    //Variable
    private var isScreenOn = true
    private val excludedApps = createExcludedAppsSet()

    override fun onCreate() {
        super.onCreate()
        appDbHelper = AppDbHelper(applicationContext)
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
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
                //Get what app is running in foreground
                currentApp = AppManager.getAppInForeground(applicationContext)
                //Check if cpu should scale or not to this app
                if (!excludedApps.contains(currentApp)) {
                    //The user could be using the same app for many seconds/minutes
                    if (lastApp != currentApp) {
                        //Save last app if any change to its values
                        //Load new app cpu values
                        lastApp = currentApp
                    }
                }
                //TODO
                //if app in excluded Set, then we need to do something
                //Decrease speed? Set to a given speed?
            } else {
                //Need to save the state of the last running app

                //Set isScreenOn to off
                isScreenOn = false
                //Set the cpu to min possible
//                cpuManager.setToMinSpeed()
            }
        }
        scheduler.scheduleAtFixedRate(runnable, 1, 2, SECONDS)
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

    private val broadcastRcv = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                //changeDetector = false;
                isScreenOn = false
            }
            if (intent.action == Intent.ACTION_SCREEN_ON) {
                isScreenOn = true
            }
        }
    }
}