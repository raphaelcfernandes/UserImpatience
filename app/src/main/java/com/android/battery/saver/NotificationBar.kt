package com.android.battery.saver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationBar {
    private val speedUpIntent = Intent("com.android.battery.saver.USER_COMPLAINED")
    private val SPEEDUPNOTIFICATION_ID = 1 //Identifier for this specific notification
    //This method creates the notification when the app is started
    fun createSpeedUpNotification(context: Context) {
        //Notification sends this on press
        val pendingSpeedUpIntent = PendingIntent.getBroadcast(context, 0, speedUpIntent, 0)

        val speedUpBuilder = NotificationCompat.Builder(context, "com.android.battery.saver")
                .setSmallIcon(R.drawable.ic_launcher_background) //Notification image
                .setContentTitle("CPU Speed Manager") //Title of notification
                .setContentText("Tap this notification to increase speed.") //Small text under title
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) //Default priority setting(subject to change)
                .setContentIntent(pendingSpeedUpIntent) //Will pass this when pressed
                .addAction(R.drawable.ic_launcher_background, "speedup", pendingSpeedUpIntent)
                .setOngoing(true) // Makes the notification stay after it's pressed

        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(SPEEDUPNOTIFICATION_ID, speedUpBuilder.build())
    }

    //This method will remove the notification from the bar when the app is killed
    fun removeNotification(context: Context) {
        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        try {
            notificationManager.cancel(SPEEDUPNOTIFICATION_ID) //Cancel is how NM removes the notification
        } catch (e: NullPointerException) {

        }

    }

}
