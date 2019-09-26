package com.android.battery.saver.helper

import java.lang.ArithmeticException
import java.lang.Exception

object FractionParser {
    fun parse(fraction: String): Double {
        if (fraction.contains("/")) {
            val number = fraction.split("/")
            val numerator = number[0]
            val denominator = number[1]
            if(denominator.toDouble() == 0.0) {
                throw Exception(ArithmeticException())
            }
            return numerator.toDouble() / denominator.toDouble()
        } else {
            throw Exception("Can't parse this number.")
        }
    }

}