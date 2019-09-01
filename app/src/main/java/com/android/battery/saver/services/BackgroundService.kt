package com.android.battery.saver.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.android.battery.saver.dao.BaseDao
import com.android.battery.saver.dao.UsageInfoDAO
import com.android.battery.saver.dao.UsageInfoDAOImpl
import com.android.battery.saver.database.UsageInfoDBHelper
import com.android.battery.saver.logger.Logger
import com.android.battery.saver.managers.AppManager
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
    private var appConfigurationArrayList = ArrayList<String>()

    override fun onCreate() {
        super.onCreate()
        usageInfoDAO = UsageInfoDAOImpl(applicationContext)
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction("com.android.battery.saver.REQUESTED_MORE_CPU")
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
        val cores = arrayListOf<Int>(1, 2, 3, 4)
        val thresh = arrayListOf<Int>(1, 2, 3, 4)
        val runnable = Runnable {
            //Check if screen is on or off
//            if (isScreenOn) {
            //Get what app is running in foreground
            currentApp = AppManager.getAppInForeground(applicationContext)
            val k = usageInfoDAO.getUsageInfoByAppName(currentApp)
//            val k = UsageInfoModel(currentApp,10,cores,thresh)
//            usageInfoDAO.insert(k)
            //Check if cpu should scale or not to this app

//                if (!excludedApps.contains(currentApp)) {
//                    //The user could be using the same app for many seconds/minutes
//                    if (lastApp != currentApp) {
//                        //Save last app if any change to its values
//                        //Load new app cpu values
//                        appConfigurationArrayList = usageInfoDBHelper.getAppData(currentApp)
//                        //Check if last app changed its configuration
//                        //Save last app to db
//                        //Check if actual app exists in db
//                        //if exists
//                        //load actual app config
//                        //else
//                        //set cpu to maximum and start decreasing
//                        lastApp = currentApp
//                    }
//                    //TODO
//                    //This will be responsible to scale down the CPU given a certain probability
//                    else {
//                        println("todo")
//                    }
//                }
            //TODO
            //if app in excluded Set, then we need to do something
            //Decrease speed? Set to a given speed?
//            } else {
//                //Need to save the state of the last running app if it changed
//                //Set isScreenOn to off
//                isScreenOn = false
//                //Set the cpu to min possible
////                cpuManager.setToMinSpeed()
//            }
        }
        scheduler.scheduleAtFixedRate(runnable, 1, 2, SECONDS)
        return START_NOT_STICKY
    }

    private fun createExcludedAppsSet(): HashSet<String> {
        val excludedAppsArrayList = arrayOf(
            "com.android.launcher",
            "com.google.android.googlequicksearchbox",
            "com.android.systemui",
            "android",
            "com.android.battery.saver"
        )
        return excludedAppsArrayList.toHashSet()
    }

    private val broadcastRcv = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                isScreenOn = false
                Log.d(Logger.DEBUG, "SCREEN OFF")
            }
            if (intent.action == Intent.ACTION_SCREEN_ON) {
                isScreenOn = true
            }
            if (intent.action == "com.android.battery.saver.REQUESTED_MORE_CPU") {
                Log.d(Logger.DEBUG, "GOTCHA")
            }
        }
    }
}