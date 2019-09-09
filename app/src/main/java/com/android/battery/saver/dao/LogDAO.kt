package com.android.battery.saver.dao

import com.android.battery.saver.model.LogModel

interface LogDAO : BaseDao<LogModel> {
    fun getDataFromAppByName(appName: String): ArrayList<LogModel>
}
