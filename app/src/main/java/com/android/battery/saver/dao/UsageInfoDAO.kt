package com.android.battery.saver.dao

import com.android.battery.saver.model.UsageInfoModel

interface UsageInfoDAO: BaseDao<UsageInfoModel> {
    fun getDataFromAppByName(appName: String): UsageInfoModel
}