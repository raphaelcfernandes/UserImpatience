package com.android.battery.saver.database

import android.provider.BaseColumns

/**
 * created by Raphael Fernandes 08/04/2019
 * Contains schema (table name and column names) for program understandability.
 */
object DBContract {
    object UsageInfo : BaseColumns {
        const val TABLE_NAME = "appCollection"
        const val APP_NAME = "APP_NAME"
        const val BRIGHTNESS = "APP_BRIGHTNESS_LEVEL"
        const val CPU = "CPU"
        const val THRESHOLD = "THRESH"
    }

    object LogInfo : BaseColumns {
        const val TABLE_NAME = "logs"
        const val APP_NAME = "APP_NAME"
        const val ID = "ID"
        const val TIMESTAMP = "TIMESTAMP"
        const val CPU = "CPU"
        const val BATTERY = "BATTERY"
    }

    object TestsInfo: BaseColumns {
        const val TABLE_NAME = "testsInfo"
        const val APP_NAME = "APP_NAME"
        const val ID = "ID"
        const val TIMESTAMP = "TIMESTAMP"
        const val CPU = "CPU"
        const val READTA = "READTA"
        const val ITERATION = "ITERATION"
        const val THRESHOLD = "THRESH"
        const val DECREASECPU_INTERVAL = "DECREASECPU_INTERVAL"
        const val DECREASECPU_FREQ = "DECREASECPU_FREQ"
        const val INCREASECPU_FREQ = "INCREASECPU_FREQ"
        const val UIMPATIENCE_LEVEL = "UIMPATIENCE_LEVEL"
    }
}