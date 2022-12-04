package com.jenny.deara.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.core.app.NotificationCompat
import com.jenny.deara.R

class NotificationHelper (base: Context?) : ContextWrapper(base) {
    private val channelId = "channelID"
    private val channelNm = "channelnm"

    init {
        // 안드로이드 버전 오레오 이상이면 채널 생성
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 채널 생성
            var channel = NotificationChannel(channelId, channelNm,
            NotificationManager.IMPORTANCE_DEFAULT)

            /*channel.enableLights(true)
            channel.enableVibration(true)
            channel.lightColor= Color.GREEN*/
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            channel.enableVibration(true)

            getManager().createNotificationChannel(channel)

        }

    }

    // NotificationManager 생성
    fun getManager(): NotificationManager{
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    // Notification 설정
    fun getChannelNotification(title: String?): NotificationCompat.Builder{

        return NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText("약 드실 시간이에요!")
            .setSmallIcon(R.drawable.logo)


    }

}