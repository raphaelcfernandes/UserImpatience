package com.android.battery.saver.managers

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import com.android.battery.saver.helper.ReadWriteFile
import java.util.*

object AppManager {

    fun getTopApp(): String {
        val k = ReadWriteFile.returnStringFromProcess(Runtime.getRuntime().exec(
                arrayOf("su", "-c", "top -n 1 | grep \" ta \" "))
        ).split("[\n]".toRegex()).forEach {
            val app = it.split(" ")
            if (app[app.size - 1] != "su" && app[app.size - 1] != "" && !app[app.size-1].contains(":"))
                return app[app.size - 1]
        }
        return ""
    }

    @Deprecated("This method shall freeze your app. Instead use getTopApp")
    fun getAppInForeground(context: Context): String {
        val arr = getArrayOfUsageStats(context)
        var greatest: Long = 0
        var topApp = ""
        for (usageStats in arr) {
            if (usageStats.lastTimeUsed > greatest) {
                topApp = usageStats.packageName
                greatest = usageStats.lastTimeUsed
            }
        }
        return topApp
    }

    private fun getArrayOfUsageStats(context: Context): ArrayList<UsageStats> {
        val usm: UsageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.MINUTE, -1)
        val startTime = calendar.timeInMillis

        return usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime) as ArrayList<UsageStats>
    }
}