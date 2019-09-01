package com.android.battery.saver.model

data class UsageInfoModel(
    var appName: String,
    var brightness: Int,
    var coreFrequencies: ArrayList<Int>,
    var coreThresholds: ArrayList<Int>
)