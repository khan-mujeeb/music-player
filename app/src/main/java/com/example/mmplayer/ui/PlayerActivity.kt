package com.example.mmplayer.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mmplayer.MainActivity.Companion.MusicList
import com.example.mmplayer.databinding.ActivityPlayerBinding
import com.example.mmplayer.modle.Music

class PlayerActivity : AppCompatActivity() {

    var binding: ActivityPlayerBinding? = null
    private lateinit var musicListPA: ArrayList<Music>
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)
        val index = intent.getIntExtra("index", 0)
        val myClass = intent.getStringExtra("class")

        when(myClass) {
            "HomeActivity" -> {
                musicListPA = MusicList
                if(mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                }

                mediaPlayer!!.reset()
                mediaPlayer!!.setDataSource(musicListPA[index].path)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding!=null) {
            binding = null
        }
    }
}