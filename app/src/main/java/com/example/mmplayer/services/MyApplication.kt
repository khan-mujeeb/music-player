package com.example.mmplayer.services

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MyApplication: Application() {

    companion object {
        val CHANNEL_ID = "channel1"
        val PLAY = "play"
        val NEXT = "next"
        val PREVIOUS = "previous"
    }

    override fun onCreate() {
        super.onCreate()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create the notification channel
            val name = "Now Playing Songs"
            val description = "This channel is important to show songs"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = description

            // Register the channel with the system
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

}