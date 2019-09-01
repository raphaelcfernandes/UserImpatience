package com.android.battery.saver.managers

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

/**
 * The impact of performing updates while the device is charging over AC is negligible,
 * so in most cases you can maximize your refresh rate whenever the device is connected to a wall charger.
 * Conversely, if the device is discharging, reducing your update rate helps prolong the battery life.
 */
class BatteryManager(context: Context) {
    private var batteryStatus = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
        context.registerReceiver(null, ifilter)
    }

    fun getBatteryLevel(): Int {
        return batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    }

    fun isBatteryCharging(): Boolean {
        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL
    }

    fun batteryChargingSource(): String {
        val chargePlug: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
            return "USB"
        }
        if(chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
            return "AC"
        }
        return "NONE"
    }

}