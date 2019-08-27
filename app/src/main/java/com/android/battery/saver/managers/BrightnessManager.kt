package com.android.battery.saver.managers

import com.android.battery.saver.helper.ReadWriteFile
import java.io.IOException

object BrightnessManager {
    //Files can be found at /sys/class/leds/lcd-backlight/
    var minLevel = 5

    fun getScreenBrightnessLevel(): Int {
        var s: String? = null
        try {
            s = ReadWriteFile.returnStringFromProcess(
                Runtime.getRuntime().exec(
                    arrayOf(
                        "su",
                        "-c",
                        "cat /sys/class/leds/lcd-backlight/brightness"
                    )
                )
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Integer.parseInt(s!!)
    }

    fun setBrightnessLevel(level: Int) {
        try {
            val proc = Runtime.getRuntime().exec(
                arrayOf(
                    "su", "-c",
                    "echo $level > /sys/class/leds/lcd-backlight/brightness"
                )
            )
            proc.waitFor()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }
}