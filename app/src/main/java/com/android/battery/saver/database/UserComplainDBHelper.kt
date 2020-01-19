package com.android.battery.saver.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.android.battery.saver.logger.Logger
import com.android.battery.saver.managers.CpuManager

class UserComplainDBHelper(context: Context) :
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
        val DATABASE_NAME = "UserComplain.db"
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
        contentValues.put(DBContract.UserComplainInfo.APP_NAME, appName)
        for (i in coreFrequencies.indices) {
            contentValues.put(DBContract.UserComplainInfo.CPU + i, coreFrequencies[i])
        }
        for (i in coreThresholds.indices) {
            contentValues.put(DBContract.UserComplainInfo.THRESHOLD + i, coreThresholds[i])
        }
        contentValues.put(DBContract.UserComplainInfo.DECREASECPU_FREQ, decreaseCpuFrequency)
        contentValues.put(DBContract.UserComplainInfo.DECREASECPU_INTERVAL, decreaseCpuInterval)
        contentValues.put(DBContract.UserComplainInfo.INCREASECPU_FREQ, increaseCpuFrequency)
        contentValues.put(DBContract.UserComplainInfo.ITERATION, iteration)
        contentValues.put(DBContract.UserComplainInfo.READTA, readTa)
        contentValues.put(DBContract.UserComplainInfo.UIMPATIENCE_LEVEL, uimpatienceLevel)
        contentValues.put(DBContract.UserComplainInfo.TIMESTAMP, System.currentTimeMillis())
        db.insert(DBContract.UserComplainInfo.TABLE_NAME, null, contentValues)
        db.close()
    }

    private fun createTable(): String {
        val createTable = StringBuilder()
        createTable.append(
                "CREATE TABLE " + DBContract.UserComplainInfo.TABLE_NAME + " (" + DBContract.UserComplainInfo.APP_NAME
                        + " TEXT, " + DBContract.UserComplainInfo.TIMESTAMP + " INT, "
                        + DBContract.UserComplainInfo.DECREASECPU_FREQ + " INT, " + DBContract.UserComplainInfo.DECREASECPU_INTERVAL + " INT, "
                        + DBContract.UserComplainInfo.INCREASECPU_FREQ + " INT, " + DBContract.UserComplainInfo.ITERATION + " INT, "
                        + DBContract.UserComplainInfo.READTA + " INT, " + DBContract.UserComplainInfo.UIMPATIENCE_LEVEL + " INT, "
        )
        for (i in 0 until CpuManager.getNumberOfCores()) {
            createTable.append(DBContract.UserComplainInfo.CPU).append(i).append(" INT, ")
        }
        for (i in 0 until CpuManager.getNumberOfCores()) {
            createTable.append(DBContract.UserComplainInfo.THRESHOLD).append(i).append(" INT, ")
        }
        createTable.setCharAt(createTable.length - 2, ')')
        return createTable.toString()
    }

    fun get(): Cursor {
        val db = this.readableDatabase
        val st = "SELECT * FROM " + DBContract.UserComplainInfo.TABLE_NAME
        return db.rawQuery(st, null)
    }
}