package com.android.battery.saver.model

/**
 * number -> The number of the core from 0-N
 * frequencies -> the frequency values this core can run on
 * currentFrequency -> the current frequency the core is running on
 */
data class CpuCoreModel(val number: Int,
                        val frequencies: ArrayList<Int>,
                        var currentFrequency: Int,
                        var governor: String,
                        var status: Boolean)