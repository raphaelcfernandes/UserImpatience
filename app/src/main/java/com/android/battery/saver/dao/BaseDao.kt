package com.android.battery.saver.dao

interface BaseDao<T> {
    fun getAll(): ArrayList<T>
    fun update(objectModel: T)
    fun insert(objectModel: T)
}