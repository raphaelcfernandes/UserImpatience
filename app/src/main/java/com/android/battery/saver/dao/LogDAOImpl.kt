package com.android.battery.saver.dao

import android.content.Context
import com.android.battery.saver.database.DBContract
import com.android.battery.saver.database.LogDBHelper
import com.android.battery.saver.managers.CpuManager
import com.android.battery.saver.model.LogModel

class LogDAOImpl(applicationContext: Context) : LogDAO {
    private var logDBHelper = LogDBHelper(applicationContext)

    override fun getDataFromAppByName(appName: String): ArrayList<LogModel> {
        val res = logDBHelper.getLogByApp(appName)
        val coreFrequencies = ArrayList<Int>()
        val arrayList = ArrayList<LogModel>()
        if (res.moveToFirst()) {
            while(res.moveToNext()){
                val app = res.getString(res.getColumnIndex(DBContract.LogInfo.APP_NAME))
                for (x in 0 until CpuManager.getNumberOfCores()) {
                    coreFrequencies.add(
                            res.getString(
                                    res.getColumnIndex(DBContract.LogInfo.CPU + x)
                            ).toInt()
                    )
                }
                val timestamp = res.getLong(res.getColumnIndex((DBContract.LogInfo.TIMESTAMP)))
                arrayList.add(LogModel(app,coreFrequencies,timestamp))
                coreFrequencies.clear()
            }
        }
        return arrayList
    }

    override fun insert(objectModel: LogModel) {
        logDBHelper.insert(
                objectModel.appName,
                objectModel.cpuFrequencies
        )
    }

}