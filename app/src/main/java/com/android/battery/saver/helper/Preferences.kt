package com.android.battery.saver.helper

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.battery.saver.logger.Logger

object Preferences {
    private const val SHARED_PREFS_FILE_NAME = "UImpatience"
    private const val KEY_PREFS_BACKGROUND_RUNNING = "background_service"
    private const val KEY_PREFS_GOVERNOR = "governor"
    private const val KEY_PREFS_READ_TA_INTERVAL = "TAinterval"
    private const val KEY_PREFS_DECREASE_CPU_TIMEINTERVAL = "decreaseCPUinterval"
    private const val KEY_PREFS_DECREASE_CPU_FREQUENCYAMOUNT = "decreaseCPUfrequency"
    private const val KEY_PREFS_INCREASE_CPU_MARGIN = "increaseCpuMargin"
    private const val KEY_PREFS_IMPATIENCE_LEVEL = "impatienceLevel"
    private const val KEY_PREFS_ITERATION = "iteration"
    private const val KEY_PREFS_TRAIN = "training"

    fun setIteration(context: Context, iteration: Int) {
        val sharedPref: SharedPreferences.Editor =
                context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putInt(KEY_PREFS_ITERATION, iteration)
        sharedPref.apply()
    }

    fun getIteration(context: Context): Int {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getInt(
                KEY_PREFS_ITERATION, -1)
    }

    fun setImpatienceLevel(context: Context, impatienceLevel: Int) {
        val sharedPref: SharedPreferences.Editor =
                context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putInt(KEY_PREFS_IMPATIENCE_LEVEL, impatienceLevel)
        sharedPref.apply()
    }

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

    fun setDecreaseCPUFrequency(context: Context, value: Int) {
        val sharedPref: SharedPreferences.Editor =
                context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putInt(KEY_PREFS_DECREASE_CPU_FREQUENCYAMOUNT, value)
        sharedPref.apply()
    }

    fun getDecreaseCPUFrequency(context: Context): Int {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getInt(
                KEY_PREFS_DECREASE_CPU_FREQUENCYAMOUNT, -1)
    }

    fun setGovernor(context: Context, governor: String) {
        val sharedPref: SharedPreferences.Editor =
                context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putString(KEY_PREFS_GOVERNOR, governor)
        sharedPref.apply()
    }

    fun setMarginToIncreaseCpu(context: Context, margin: Int) {
        val sharedPref: SharedPreferences.Editor =
                context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putInt(KEY_PREFS_INCREASE_CPU_MARGIN, margin)
        sharedPref.apply()
    }

    fun setBackgroundServiceStatus(context: Context, state: Boolean) {
        val sharedPref: SharedPreferences.Editor =
                context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putString(KEY_PREFS_BACKGROUND_RUNNING, state.toString())
        sharedPref.apply()
    }

    fun setTrainer(context: Context, state: Boolean) {
        val sharedPref : SharedPreferences.Editor = context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putBoolean(KEY_PREFS_TRAIN, state)
        sharedPref.apply()
    }

    fun getImpatienceLevel(context: Context): Int {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getInt(
                KEY_PREFS_IMPATIENCE_LEVEL, -1)
    }

    fun getBackgroundServiceStatus(context: Context): String? {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getString(
                KEY_PREFS_BACKGROUND_RUNNING, null)
    }

    fun getMarginToIncreaseCpu(context: Context): Int {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getInt(
                KEY_PREFS_INCREASE_CPU_MARGIN, 0)
    }

    fun getReadTAInterval(context: Context): Int {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getInt(
                KEY_PREFS_READ_TA_INTERVAL, 0)
    }

    fun getGovernor(context: Context): String? {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getString(
                KEY_PREFS_GOVERNOR, null)
    }

    fun getDecreaseCPUInterval(context: Context): Int {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getInt(
                KEY_PREFS_DECREASE_CPU_TIMEINTERVAL, 0)
    }

    fun getTraining(context: Context):  Boolean {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).getBoolean(KEY_PREFS_TRAIN, false)
    }

    fun clearPreferences(context: Context) {
        context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE).edit().clear().apply()
    }
}