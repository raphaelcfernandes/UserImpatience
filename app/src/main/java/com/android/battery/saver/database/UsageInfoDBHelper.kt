package com.android.battery.saver.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.android.battery.saver.managers.CpuManager


class UsageInfoDBHelper(context: Context) :
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
        val DATABASE_NAME = "UImpatience.db"
        val DATABASE_VERSION = 1
    }

    fun insertAppConfiguration(
            appName: String, brightnessLevel: Int,
            coreFrequencies: ArrayList<Int>,
            coreThresholds: ArrayList<Int>
    ) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBContract.UsageInfo.APP_NAME, appName)
        contentValues.put(DBContract.UsageInfo.BRIGHTNESS, brightnessLevel)
        for (i in coreFrequencies.indices) {
            contentValues.put(DBContract.UsageInfo.CPU + i, coreFrequencies[i])
        }
        for (i in coreThresholds.indices) {
            contentValues.put(DBContract.UsageInfo.THRESHOLD + i, coreThresholds[i])
        }
        db.insert(DBContract.UsageInfo.TABLE_NAME, null, contentValues)
    }

    private fun checkIfDataAlreadyExists(appName: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
                "SELECT * FROM " + DBContract.UsageInfo.TABLE_NAME + " WHERE "
                        + DBContract.UsageInfo.APP_NAME + " LIKE ?", arrayOf(appName)
        )
        if (cursor.count <= 0) {
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    /**
     * Updates a specific app with new values
     */
    fun updateAppConfiguration(
            appName: String,
            brightnessLevel: Int,
            coreFrequencies: ArrayList<Int>,
            coreThresholds: ArrayList<Int>
    ) {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBContract.UsageInfo.BRIGHTNESS, brightnessLevel)
        for (x in coreFrequencies.indices) {
            contentValues.put(DBContract.UsageInfo.CPU + x, coreFrequencies[x])
        }
        for (x in coreThresholds.indices) {
            contentValues.put(DBContract.UsageInfo.THRESHOLD + x, coreThresholds[x])
        }
        db.update(
                DBContract.UsageInfo.TABLE_NAME,
                contentValues,
                "appName = ? ",
                arrayOf(appName)
        )
    }

    fun getAppData(AppName: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
                "SELECT * FROM " + DBContract.UsageInfo.TABLE_NAME + " WHERE "
                        + DBContract.UsageInfo.APP_NAME + " LIKE ?", arrayOf(AppName)
        )
    }

    private fun createTable(): String {
        val createTable = StringBuilder()
        createTable.append(
                "CREATE TABLE " + DBContract.UsageInfo.TABLE_NAME + " (" + DBContract.UsageInfo.APP_NAME
                        + " TEXT PRIMARY KEY, " + DBContract.UsageInfo.BRIGHTNESS + " INT, "
        )
        for (i in 0 until CpuManager.getNumberOfCores()) {
            createTable.append(DBContract.UsageInfo.CPU).append(i).append(" INT, ")
        }
        for (i in 0 until CpuManager.getNumberOfCores()) {
            createTable.append(DBContract.UsageInfo.THRESHOLD).append(i).append(" INT, ")
        }
        createTable.setCharAt(createTable.length - 2, ')')
        return createTable.toString()
    }
}