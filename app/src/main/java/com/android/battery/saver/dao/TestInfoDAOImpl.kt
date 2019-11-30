package com.android.battery.saver.dao

import android.content.Context
import android.util.Log
import com.android.battery.saver.database.DBContract
import com.android.battery.saver.database.TestInfoDBHelper
import com.android.battery.saver.logger.Logger
import com.android.battery.saver.managers.CpuManager
import com.android.battery.saver.model.TestsInfoModel


class TestInfoDAOImpl(applicationContext: Context) : TestsInfoDAO {
    private var testInfoDBHelper: TestInfoDBHelper = TestInfoDBHelper(applicationContext)

    override fun insert(objectModel: TestsInfoModel) {
        testInfoDBHelper.insert(
                objectModel.appName,
                objectModel.coreFrequencies,
                objectModel.coreThresholds,
                objectModel.decreaseCpuFrequency,
                objectModel.decreaseCpuIterval,
                objectModel.increaseCpuFrequency,
                objectModel.iteration,
                objectModel.readTa,
                objectModel.uimpatienceLevel
        )
    }

    fun get(): TestsInfoModel? {
        val res = testInfoDBHelper.get()
        Log.d(Logger.DEBUG, res.count.toString())
        val coreFrequencies = ArrayList<Int>()
        val coreThresholds = ArrayList<Int>()
        if (res.moveToFirst()) {

            val app = res.getString(res.getColumnIndex(DBContract.TestsInfo.APP_NAME))
            val decreaseCpuf = res.getInt(res.getColumnIndex(DBContract.TestsInfo.DECREASECPU_FREQ))
            val decreaseCpuI = res.getInt(res.getColumnIndex(DBContract.TestsInfo.DECREASECPU_INTERVAL))
            val increaseCpuF = res.getInt(res.getColumnIndex(DBContract.TestsInfo.INCREASECPU_FREQ))
            val iteration = res.getInt(res.getColumnIndex(DBContract.TestsInfo.ITERATION))
            val readTa = res.getInt(res.getColumnIndex(DBContract.TestsInfo.READTA))
            val uimpatience = res.getInt(res.getColumnIndex(DBContract.TestsInfo.UIMPATIENCE_LEVEL))
            val timestamp = res.getLong(res.getColumnIndex(DBContract.TestsInfo.TIMESTAMP))

            for (x in 0 until CpuManager.getNumberOfCores()) {
                coreFrequencies.add(
                        res.getString(
                                res.getColumnIndex(DBContract.TestsInfo.CPU + x)
                        ).toInt()
                )
            }
            for (x in 0 until CpuManager.getNumberOfCores()) {
                coreThresholds.add(
                        res.getString(
                                res.getColumnIndex(
                                        DBContract.TestsInfo.THRESHOLD + x
                                )
                        ).toInt()
                )
            }
            return TestsInfoModel(app, coreFrequencies, coreThresholds, readTa, iteration,
                    decreaseCpuI, decreaseCpuf, increaseCpuF, uimpatience, timestamp)
        }
        return null
    }
}