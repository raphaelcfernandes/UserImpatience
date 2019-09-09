package com.android.battery.saver.dao

interface BaseDao<T> {
    fun insert(objectModel: T)
}