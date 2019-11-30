package com.android.battery.saver.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.android.battery.saver.logger.Logger
import com.android.battery.saver.managers.CpuManager

class TestInfoDBHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTable())
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Defines the database schema and version
     */
    companion object {
        val DATABASE_NAME = "TestInfo.db"
        val DATABASE_VERSION = 1
    }

    fun insert(
            appName: String,
            coreFrequencies: ArrayList<Int>,
            coreThresholds: ArrayList<Int>,
            decreaseCpuFrequency: Int,
            decreaseCpuInterval: Int,
            increaseCpuFrequency: Int,
            iteration: Int,
            readTa: Int,
            uimpatienceLevel: Int
    ) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBContract.TestsInfo.APP_NAME, appName)
        for (i in coreFrequencies.indices) {
            contentValues.put(DBContract.TestsInfo.CPU + i, coreFrequencies[i])
        }
        for (i in coreThresholds.indices) {
            contentValues.put(DBContract.TestsInfo.THRESHOLD + i, coreThresholds[i])
        }
        contentValues.put(DBContract.TestsInfo.DECREASECPU_FREQ, decreaseCpuFrequency)
        contentValues.put(DBContract.TestsInfo.DECREASECPU_INTERVAL, decreaseCpuInterval)
        contentValues.put(DBContract.TestsInfo.INCREASECPU_FREQ, increaseCpuFrequency)
        contentValues.put(DBContract.TestsInfo.ITERATION, iteration)
        contentValues.put(DBContract.TestsInfo.READTA, readTa)
        contentValues.put(DBContract.TestsInfo.UIMPATIENCE_LEVEL, uimpatienceLevel)
        contentValues.put(DBContract.TestsInfo.TIMESTAMP, System.currentTimeMillis())
        db.insert(DBContract.TestsInfo.TABLE_NAME, null, contentValues)
    }

    private fun createTable(): String {
        Log.d(Logger.DEBUG,"Creating DATABASE TESTINFO")
        val createTable = StringBuilder()
        createTable.append(
                "CREATE TABLE " + DBContract.TestsInfo.TABLE_NAME + " (" + DBContract.TestsInfo.APP_NAME
                        + " TEXT, " + DBContract.TestsInfo.TIMESTAMP + " INT, "
                        + DBContract.TestsInfo.DECREASECPU_FREQ + " INT, " + DBContract.TestsInfo.DECREASECPU_INTERVAL + " INT, "
                        + DBContract.TestsInfo.INCREASECPU_FREQ + " INT, " + DBContract.TestsInfo.ITERATION + " INT, "
                        + DBContract.TestsInfo.READTA + " INT, " + DBContract.TestsInfo.UIMPATIENCE_LEVEL + " INT, "
        )
        for (i in 0 until CpuManager.getNumberOfCores()) {
            createTable.append(DBContract.TestsInfo.CPU).append(i).append(" INT, ")
        }
        for (i in 0 until CpuManager.getNumberOfCores()) {
            createTable.append(DBContract.TestsInfo.THRESHOLD).append(i).append(" INT, ")
        }
        createTable.setCharAt(createTable.length - 2, ')')
        return createTable.toString()
    }

    fun get(): Cursor {
        val db = this.readableDatabase
        val st = "SELECT * FROM " + DBContract.TestsInfo.TABLE_NAME
        return db.rawQuery(st, null)
    }
}