package com.android.battery.saver.helper

object FractionParser {
    fun parse(fraction: Int): Double {
        return 1/fraction.toDouble()
    }

}