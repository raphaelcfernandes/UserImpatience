package com.android.battery.saver.model

data class LogModel(val appName: String,
                    val cpuFrequencies: ArrayList<Int>,
                    val timestamp: Long
)