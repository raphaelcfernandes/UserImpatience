package com.android.battery.saver.dao

import android.content.Context
import android.util.Log
import com.android.battery.saver.database.DBContract
import com.android.battery.saver.database.UserComplainDBHelper
import com.android.battery.saver.logger.Logger
import com.android.battery.saver.managers.CpuManager
import com.android.battery.saver.model.UserComplainModel

class UserComplainDAOImpl(applicationContext: Context) : UserComplainDAO {
    private var userComplainDBHelper: UserComplainDBHelper = UserComplainDBHelper(applicationContext)

    override fun insert(objectModel: UserComplainModel) {
        userComplainDBHelper.insert(
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

    fun get(): UserComplainModel? {
        val res = userComplainDBHelper.get()
        Log.d(Logger.DEBUG, res.count.toString())
        val coreFrequencies = ArrayList<Int>()
        val coreThresholds = ArrayList<Int>()
        if (res.moveToFirst()) {

            val app = res.getString(res.getColumnIndex(DBContract.UserComplainInfo.APP_NAME))
            val decreaseCpuf = res.getInt(res.getColumnIndex(DBContract.UserComplainInfo.DECREASECPU_FREQ))
            val decreaseCpuI = res.getInt(res.getColumnIndex(DBContract.UserComplainInfo.DECREASECPU_INTERVAL))
            val increaseCpuF = res.getInt(res.getColumnIndex(DBContract.UserComplainInfo.INCREASECPU_FREQ))
            val iteration = res.getInt(res.getColumnIndex(DBContract.UserComplainInfo.ITERATION))
            val readTa = res.getInt(res.getColumnIndex(DBContract.UserComplainInfo.READTA))
            val uimpatience = res.getInt(res.getColumnIndex(DBContract.UserComplainInfo.UIMPATIENCE_LEVEL))
            val timestamp = res.getLong(res.getColumnIndex(DBContract.UserComplainInfo.TIMESTAMP))

            for (x in 0 until CpuManager.getNumberOfCores()) {
                coreFrequencies.add(
                        res.getString(
                                res.getColumnIndex(DBContract.UserComplainInfo.CPU + x)
                        ).toInt()
                )
            }
            for (x in 0 until CpuManager.getNumberOfCores()) {
                coreThresholds.add(
                        res.getString(
                                res.getColumnIndex(
                                        DBContract.UserComplainInfo.THRESHOLD + x
                                )
                        ).toInt()
                )
            }
            return UserComplainModel(app, coreFrequencies, coreThresholds, readTa, iteration,
                    decreaseCpuI, decreaseCpuf, increaseCpuF, uimpatience, timestamp)
        }
        return null
    }
}