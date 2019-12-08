package com.android.battery.saver.event

data class OnCpuScaleAllDownEvent(val currentApp: String,
                                  val readInterval: Int,
                                  val iteration: Int,
                                  val decreaseCpuInterval: Int,
                                  val decreaseCpuFrequency: Int,
                                  val increaseCpuFrequency: Int,
                                  val UImpatienceLevel: Int)