package com.android.battery.saver.ui

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.android.battery.saver.R
import com.android.battery.saver.activities.FeedBackPopUpWindow
import java.util.*

//TODO: ask permission to draw over other apps
//TODO: This should open a modal and not start another activiy
//Too intrusive
@Deprecated("This Class is intrusive. Avoid using it as users will be annoyed")
object BubbleButton {
    private lateinit var windowManager: WindowManager
    private lateinit var floatingButton: ImageView
    private val CUSTOM_INTENT = "com.example.raphael.tcc.CreateMenuWindow"
    fun createFeedbackButton(context: Context) {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        floatingButton = ImageView(context)
        floatingButton.setImageResource(R.mipmap.ic_cross_symbol)
        val LAYOUT_FLAG: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_APPLICATION
        }
        val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 1006
        params.y = 606
        windowManager.addView(floatingButton, params)
        floatingButton.setOnTouchListener(object : View.OnTouchListener {
            private val MAX_CLICK_DURATION = 100
            private var startClickTime: Long = 0
            private var initialX: Int = 0
            private var initialY: Int = 0
            private var initialTouchX: Float = 0.toFloat()
            private var initialTouchY: Float = 0.toFloat()


            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        startClickTime = Calendar.getInstance().timeInMillis
                    }
                    MotionEvent.ACTION_UP -> if (Calendar.getInstance().timeInMillis - startClickTime < MAX_CLICK_DURATION) {
                        Intent().also { intent ->
                            intent.setAction("com.android.battery.saver.USER_COMPLAINED")
//                            sendBroadcast(intent)
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - initialTouchX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager.updateViewLayout(floatingButton, params)
                    }
                }
                return true
            }
        })
    }

    fun removeView() {
        try {
            windowManager.removeView(floatingButton)
        } catch (e: NullPointerException) {

        }

    }
}