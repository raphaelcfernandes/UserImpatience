package com.android.battery.saver.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.android.battery.saver.managers.CpuManager
import java.util.*

class LogDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "logs.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTable())
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    private fun createTable(): String {
        val createTable = StringBuilder()
        createTable.append(
            "CREATE TABLE " + DBContract.LogInfo.TABLE_NAME + " (" + DBContract.LogInfo.ID +
                    " INT PRIMARY KEY, " + DBContract.LogInfo.APP_NAME + " TEXT, "
                    + DBContract.LogInfo.TIMESTAMP + " INTEGER, "
        )
        for (i in 0 until CpuManager.getNumberOfCores()) {
            createTable.append(DBContract.LogInfo.CPU).append(i).append(" INT, ")
        }
        createTable.setCharAt(createTable.length - 2, ')')
        return createTable.toString()
    }

    /**
     * Receive the name of the app and an array of core frequency and insert to the table LogInfo
     * THE TIMESTAMP INSERTED HERE IS IN MILLISECONDS
     * @param appName: the name of the app that user complained
     * @param cpuSpeed: arrayList that contains the CPUs value when the user complained
     */
    fun insertLog(appName: String, cpuSpeed: ArrayList<Int>) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBContract.LogInfo.APP_NAME, appName)
        val d = Date()
        //MILLISECONDS TIMESTAMP
        contentValues.put(DBContract.LogInfo.TIMESTAMP, d.time)
        for (i in cpuSpeed.indices) {
            contentValues.put(DBContract.LogInfo.CPU + i, cpuSpeed[i])
        }
        db.insert(DBContract.LogInfo.TABLE_NAME, null, contentValues)
    }
}