package com.android.battery.saver.event

data class OnGovernorSelectedEvent(
        val governor: String,
        val readTA: String,
        val timeWindow: String
)