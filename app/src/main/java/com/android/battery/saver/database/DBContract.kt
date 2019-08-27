package com.android.battery.saver.database

import android.provider.BaseColumns

/**
 * created by Raphael Fernandes 08/04/2019
 * Contains schema (table name and column names) for program understandability.
 */
object DBContract {
    object usageInfo : BaseColumns {
        val TABLE_NAME = "appCollection"
        val APP_NAME = "APP_NAME"
        val BRIGHTNESS = "APP_BRIGHTNESS_LEVEL"
        val CORE = "CPU"
        val THRESHOLD = "THRESH"
    }
}