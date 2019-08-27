package com.android.battery.saver.activities

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.android.battery.saver.R

class FeedBackPopUpWindow : AppCompatActivity() {
    private var isProgressBarBrightnessMoved = false
    private var isProgressBarCpuMoved = false
//    private var seekBarBrightness: DiscreteSeekBar? = null
//    private var seekBarCpu: DiscreteSeekBar? = null
//    private val brightnessManager = BrightnessManager()


    public override fun onCreate(savedInstancedBundle: Bundle?) {
        super.onCreate(savedInstancedBundle)
        setContentView(R.layout.activity_feedbackpopupwindow)

        /**
         * SeekBar Brightness
         */
//        seekBarBrightness = findViewById(R.id.seekBarBrightness)
//        seekBarBrightness!!.setMin(0)
//        seekBarBrightness!!.setProgress(brightnessManager.getScreenBrightnessLevel() * 100 / 255)
//        seekBarBrightness!!.setMax(100)//Percentage
//        seekBarBrightness!!.setOnProgressChangeListener(object :
//            DiscreteSeekBar.OnProgressChangeListener() {
//            internal var onProgressChanged = 0
//
//            fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
//                onProgressChanged = value
//                isProgressBarBrightnessMoved = true
//            }
//
//            fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {
//
//            }
//
//            fun onStopTrackingTouch(seekBar: DiscreteSeekBar) {}
//        })


        /**
         * SeekBar CPU
         */
//        seekBarCpu = findViewById(R.id.seekBarCpu)
//        seekBarCpu!!.setMin(1)
//        seekBarCpu!!.setProgress(`object`.getSumNumberCore())
//        seekBarCpu!!.setMax(100)
//        seekBarCpu!!.setOnProgressChangeListener(object :
//            DiscreteSeekBar.OnProgressChangeListener() {
//            internal var onProgressChanged = 0
//
//            fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
//                cpuBarValue = value
//                onProgressChanged = cpuBarValue
//                isProgressBarCpuMoved = true
//            }
//
//            fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {
//
//            }
//
//            fun onStopTrackingTouch(seekBar: DiscreteSeekBar) {}
//        })
        val displaymetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displaymetrics)
        val height = displaymetrics.heightPixels
        val width = displaymetrics.widthPixels
        val params = window.attributes
        params.x = -20
        params.height = height / 3
        params.width = width / 2
        params.y = -10
        this.window.attributes = params
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (isProgressBarBrightnessMoved) {
//            if (seekBarBrightness!!.getProgress() == 0)
//                brightnessManager.setBrightnessLevel(255 / 100)
//            else
//                brightnessManager.setBrightnessLevel(seekBarBrightness!!.getProgress() * 255 / 100)
        }
        //Create Intent and send to BackgroundService -> ButtonClicked
        if (isProgressBarCpuMoved) {
            val i = Intent("com.example.raphael.tcc.REQUESTED_MORE_CPU")
            i.putExtra("valorCpuUsuario", cpuBarValue)
            sendBroadcast(i)
        }
    }

    companion object {
        private var cpuBarValue: Int = 0
    }
}