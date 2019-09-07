package com.android.battery.saver.dao

import android.content.Context
import com.android.battery.saver.database.DBContract
import com.android.battery.saver.database.UsageInfoDBHelper
import com.android.battery.saver.managers.CpuManager
import com.android.battery.saver.model.UsageInfoModel

class UsageInfoDAOImpl(applicationContext: Context) : UsageInfoDAO {
    private var appDbHelper: UsageInfoDBHelper = UsageInfoDBHelper(applicationContext)

    override fun getAll(): ArrayList<UsageInfoModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(objectModel: UsageInfoModel) {
        appDbHelper.updateAppConfiguration(
                objectModel.appName,
                objectModel.brightness,
                objectModel.coreFrequencies,
                objectModel.coreThresholds)
    }

    override fun insert(objectModel: UsageInfoModel) {
        appDbHelper.insertAppConfiguration(
                objectModel.appName,
                objectModel.brightness,
                objectModel.coreFrequencies,
                objectModel.coreThresholds
        )
    }

    fun getUsageInfoByAppName(appName: String): UsageInfoModel {
        val res = appDbHelper.getAppData(appName)
        val coreFrequencies = ArrayList<Int>()
        val coreThresholds = ArrayList<Int>()
        if (res.moveToFirst()) {
            val app = res.getString(res.getColumnIndex(DBContract.UsageInfo.APP_NAME))
            val brightness =
                    res.getString(res.getColumnIndex(DBContract.UsageInfo.BRIGHTNESS)).toInt()
            for (x in 0 until CpuManager.getNumberOfCores()) {
                coreFrequencies.add(
                        res.getString(
                                res.getColumnIndex(DBContract.UsageInfo.CPU + x)
                        ).toInt()
                )
            }
            for (x in 0 until CpuManager.getNumberOfCores()) {
                coreThresholds.add(
                        res.getString(
                                res.getColumnIndex(
                                        DBContract.UsageInfo.THRESHOLD + x
                                )
                        ).toInt()
                )
            }
            return UsageInfoModel(app, brightness, coreFrequencies, coreThresholds)
        }
        return UsageInfoModel("", 0, arrayListOf(), arrayListOf())
    }
}