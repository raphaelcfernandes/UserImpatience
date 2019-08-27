package com.android.battery.saver.managers

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.*

object AppManager {

    fun getAppInForeground(context: Context): String {
        val arr = getArrayOfUsageStats(context)
        val mySortedMap = TreeMap<Long, UsageStats>()
        for (usageStats in arr) {
            mySortedMap[usageStats.getLastTimeUsed()] = usageStats
        }
        return mySortedMap[mySortedMap.lastKey()]!!.getPackageName()
    }

    private fun getArrayOfUsageStats(context: Context): ArrayList<UsageStats> {
        val usm: UsageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        val startTime = calendar.timeInMillis
        calendar.add(Calendar.MINUTE, -1)
        val endTime = calendar.timeInMillis

        return usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, endTime, startTime) as ArrayList<UsageStats>
    }
}