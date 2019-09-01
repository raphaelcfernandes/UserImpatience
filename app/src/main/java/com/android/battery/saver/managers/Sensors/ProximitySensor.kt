package com.android.battery.saver.managers.Sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class ProximitySensor(context: Context) : SensorEventListener {

    private var sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var mProximity: Sensor? = null

    init {
        mProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        mProximity?.also { proximity ->
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        println("bbla")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        println(event!!.values[0])
    }

}