package com.android.battery.saver.database

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.android.battery.saver.database.UsageInfoDBHelper.Companion.DATABASE_NAME
import com.android.battery.saver.database.UsageInfoDBHelper.Companion.DATABASE_VERSION

abstract class BaseDBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)  {

}