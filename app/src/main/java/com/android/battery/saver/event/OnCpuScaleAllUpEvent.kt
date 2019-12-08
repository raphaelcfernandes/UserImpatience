package com.android.battery.saver.event

data class OnCpuScaleAllUpEvent(val increase: Int,
                                val currentApp: String,
                                val readInterval: Int,
                                val iteration: Int,
                                val decreaseCpuInterval: Int,
                                val decreaseCpuFrequency: Int,
                                val increaseCpuFrequency: Int,
                                val UImpatienceLevel: Int)