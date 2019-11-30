package com.android.battery.saver.model

data class TestsInfoModel(val appName: String,
                          val coreFrequencies: ArrayList<Int>,
                          val coreThresholds: ArrayList<Int>,
                          val readTa: Int,
                          val iteration: Int,
                          val decreaseCpuIterval: Int,
                          val decreaseCpuFrequency: Int,
                          val increaseCpuFrequency: Int,
                          val uimpatienceLevel: Int,
                          val timestamp: Long = 0
)
