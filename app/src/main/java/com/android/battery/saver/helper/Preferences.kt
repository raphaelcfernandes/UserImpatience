package com.android.battery.saver.helper

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private val SHARED_PREFS_FILE_NAME = "UImpatience"
    private val KEY_PREFS_BACKGROUND_RUNNING = "background_service"
    private val KEY_PREFS_GOVERNOR = "governor"
    private val KEY_PREFS_READ_TA_INTERVAL = "TAinterval"
    private val KEY_PREFS_DECREASE_CPU_TIMEINTERVAL = "decreaseCPUinterval"

    fun setReadTAInterval(context: Context, interval: Int) {
        val sharedPref: SharedPreferences.Editor =
                context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putInt(KEY_PREFS_READ_TA_INTERVAL, interval)
        sharedPref.apply()
    }

    fun setDecreaseCPUInterval(context: Context, interval: Int) {
        val sharedPref: SharedPreferences.Editor =
                context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putInt(KEY_PREFS_DECREASE_CPU_TIMEINTERVAL, interval)
        sharedPref.apply()
    }

    fun setGovernor(context: Context, governor: String) {
        val sharedPref: SharedPreferences.Editor =
                context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putString(KEY_PREFS_GOVERNOR, governor)
        sharedPref.apply()
    }

    fun setBackgroundServiceStatus(context: Context, state: Boolean) {
        val sharedPref: SharedPreferences.Editor =
                context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putString(KEY_PREFS_BACKGROUND_RUNNING, state.toString())
        sharedPref.apply()
    }

    fun getBackgroundServiceStatus(context: Context): String? {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getString(
                KEY_PREFS_BACKGROUND_RUNNING, null)
    }

    fun getReadTAInterval(context: Context): Int? {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getInt(
                KEY_PREFS_READ_TA_INTERVAL, -1)
    }

    fun getGovernor(context: Context): String? {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getString(
                KEY_PREFS_GOVERNOR, null)
    }

    fun getDecreaseCPUInterval(context: Context): Int? {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getInt(
                KEY_PREFS_DECREASE_CPU_TIMEINTERVAL, -1)
    }

    fun clearPreferences(context: Context) {
        context.getSharedPreferences(SHARED_PREFS_FILE_NAME,Context.MODE_PRIVATE).edit().clear().apply()
    }
}