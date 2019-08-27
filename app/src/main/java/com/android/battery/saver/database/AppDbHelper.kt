package com.android.battery.saver.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.android.battery.saver.managers.CpuManager


class AppDbHelper(context: Context) :
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
        appName: String, brightnessLevel: Int, cpuSpeed: ArrayList<Int>,
        thresholds: ArrayList<Int>
    ) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBContract.usageInfo.APP_NAME, appName)
        contentValues.put(DBContract.usageInfo.BRIGHTNESS, brightnessLevel)
        for (i in cpuSpeed.indices) {
            contentValues.put(DBContract.usageInfo.CORE + i, cpuSpeed[i])
        }
        for (i in thresholds.indices) {
            contentValues.put(DBContract.usageInfo.THRESHOLD + i, thresholds[i])
        }
        db.insert(DBContract.usageInfo.TABLE_NAME, null, contentValues)
    }

    private fun checkIfDataAlreadyExists(appName: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM " + DBContract.usageInfo.TABLE_NAME + " WHERE "
                    + DBContract.usageInfo.APP_NAME + " LIKE ?", arrayOf(appName)
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
        cpuSpeed: ArrayList<Int>,
        thresholds: ArrayList<Int>
    ) {
        if (!checkIfDataAlreadyExists(appName))
            insertAppConfiguration(appName, brightnessLevel, cpuSpeed, thresholds)
        else {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(DBContract.usageInfo.BRIGHTNESS, brightnessLevel)
            for (x in cpuSpeed.indices) {
                contentValues.put(DBContract.usageInfo.CORE + x, cpuSpeed[x])
            }
            for (x in thresholds.indices) {
                contentValues.put(DBContract.usageInfo.THRESHOLD + x, thresholds[x])
            }
            db.update(
                DBContract.usageInfo.TABLE_NAME,
                contentValues,
                "appName = ? ",
                arrayOf(appName)
            )
        }
    }

    fun getAppData(numberOfCores: Int, AppName: String): java.util.ArrayList<String> {
        val arrayList = java.util.ArrayList<String>()
        val db = this.readableDatabase
        val res = db.rawQuery(
            "SELECT * FROM " + DBContract.usageInfo.TABLE_NAME + " WHERE "
                    + DBContract.usageInfo.APP_NAME + " LIKE ?", arrayOf(AppName)
        )
        if (res.moveToFirst()) {
            arrayList.add(res.getString(res.getColumnIndex(DBContract.usageInfo.APP_NAME)))
            arrayList.add(res.getString(res.getColumnIndex(DBContract.usageInfo.BRIGHTNESS)))
            for (x in 0 until numberOfCores) {
                arrayList.add(res.getString(res.getColumnIndex(DBContract.usageInfo.CORE + x)))
            }
            for (x in 0 until numberOfCores) {
                arrayList.add(res.getString(res.getColumnIndex(DBContract.usageInfo.THRESHOLD + x)))
            }
        }
        res.close()
        return arrayList
    }

    private fun createTable(): String {
        val createTable = StringBuilder()
        createTable.append(
            "CREATE TABLE " + DBContract.usageInfo.TABLE_NAME + " (" + DBContract.usageInfo.APP_NAME
                    + " TEXT PRIMARY KEY, " + DBContract.usageInfo.BRIGHTNESS + " INT, "
        )
        for (i in 0 until CpuManager.getNumberOfCores()) {
            createTable.append(DBContract.usageInfo.CORE).append(i).append(" INT, ")
        }
        for (i in 0 until CpuManager.getNumberOfCores()) {
            createTable.append(DBContract.usageInfo.THRESHOLD).append(i).append(" INT, ")
        }
        createTable.setCharAt(createTable.length - 2, ')')
        return createTable.toString()
    }
}