package com.example.mmplayer.services

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.mmplayer.R
import com.example.mmplayer.ui.PlayerActivity.Companion.index
import com.example.mmplayer.ui.PlayerActivity.Companion.musicListPA

class MusicService: Service() {

    val myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? =null
    private lateinit var mediaSession: MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    inner class MyBinder: Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification() {
        val notification = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
            .setContentTitle(musicListPA[index].title)
            .setContentText(musicListPA[index].artist)
            .setSmallIcon(R.drawable.ic_baseline_library_music_24)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_music_note_24))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.baseline_skip_previous_24, "previous", null)
            .addAction(R.drawable.baseline_play_arrow_24, "play", null)
            .addAction(R.drawable.baseline_skip_next_24, "next", null)
            .build()

        startForeground(18, notification)
    }
}