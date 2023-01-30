package com.example.mmplayer.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mmplayer.MainActivity.Companion.MusicList
import com.example.mmplayer.R
import com.example.mmplayer.databinding.ActivityPlayerBinding
import com.example.mmplayer.modle.Music

class PlayerActivity : AppCompatActivity() {

    var binding: ActivityPlayerBinding? = null
    private lateinit var musicListPA: ArrayList<Music>
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)
        val index = intent.getIntExtra("index", 0)
        val myClass = intent.getStringExtra("class")

        binding!!.playPause.setOnClickListener {
            if (isPlaying) {
                pause()
            }else {
                play()
            }
        }

        when(myClass) {
            "HomeActivity" -> {
                musicListPA = MusicList
                createMediaPlayer(index)

            }
        }
    }

    private fun createMediaPlayer(index: Int) {
        try {
            if(mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }

            setMusicData(index)
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(musicListPA[index].path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            binding!!.playPause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
        }catch (e: java.lang.Exception) {
            return
        }
    }

    private fun play() {
        isPlaying = true
        binding!!.playPause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
        mediaPlayer!!.start()
    }
    private fun pause() {
        isPlaying = false
        binding!!.playPause.setImageResource(R.drawable.ic_baseline_play_circle_24)
        mediaPlayer!!.pause()
    }

    private fun setMusicData(index: Int) {

        binding!!.title.text = musicListPA[index].title
        binding!!.artist.text = musicListPA[index].artist

        // album art loading
        Glide.with(this)
            .load(musicListPA[index].url)
            .into(binding!!.albumArt)

    }


    override fun onDestroy() {
        super.onDestroy()
        if (binding!=null) {
            binding = null
        }
    }
}