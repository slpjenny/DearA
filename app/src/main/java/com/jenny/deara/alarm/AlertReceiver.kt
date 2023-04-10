package com.jenny.deara.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.*

class AlertReceiver: BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            // Set the alarm here.
            var title = intent?.getStringExtra("title")
            var week = intent?.getBooleanArrayExtra("week")

            var cal = Calendar.getInstance()

            if(week!![cal.get(Calendar.DAY_OF_WEEK) - 1] == true) {
                var notificationHelper: NotificationHelper = NotificationHelper(context)
                var nb: NotificationCompat.Builder = notificationHelper.getChannelNotification(title)
                //알림 호출
                notificationHelper.getManager().notify(1, nb.build())
            }

        } else {
            var title = intent?.getStringExtra("title")
            var week = intent?.getBooleanArrayExtra("week")

            var cal = Calendar.getInstance()

            if(week!![cal.get(Calendar.DAY_OF_WEEK) - 1] == true) {
                var notificationHelper: NotificationHelper = NotificationHelper(context)
                var nb: NotificationCompat.Builder = notificationHelper.getChannelNotification(title)
                //알림 호출
                notificationHelper.getManager().notify(1, nb.build())
            }
        }

        var title = intent?.getStringExtra("title")
        var week = intent?.getBooleanArrayExtra("week")

        var cal = Calendar.getInstance()

        if(week!![(cal.get(Calendar.DAY_OF_WEEK) - 1)] == true) {
            var notificationHelper: NotificationHelper = NotificationHelper(context)
            var nb: NotificationCompat.Builder = notificationHelper.getChannelNotification(title)
            //알림 호출
            notificationHelper.getManager().notify(1, nb.build())
        }

    }
}