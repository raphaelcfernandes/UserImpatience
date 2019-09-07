package com.android.battery.saver.model

/**
 * number -> The number of the core from 0-N
 * frequencies -> the frequency values this core can run on
 * currentFrequency -> the current frequency the core is running on
 * governor -> The governor that is controlling the core
 * status -> If core is on (1) or off (0)
 * threshold -> If the user complained, this core will have the last frequency before complaining
 */
data class CpuCoreModel(val number: Int,
                        val frequencies: ArrayList<Int>,
                        var currentFrequency: Int,
                        var governor: String,
                        var freqPos: Int,
                        var status: Boolean,
                        var threshold: Int)