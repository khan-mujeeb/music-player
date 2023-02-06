package com.example.mmplayer.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.example.mmplayer.MainActivity.Companion.MusicList
import com.example.mmplayer.R
import com.example.mmplayer.databinding.ActivityPlayerBinding
import com.example.mmplayer.modle.Music
import com.example.mmplayer.services.MusicService

class PlayerActivity : AppCompatActivity(), ServiceConnection {

    var binding: ActivityPlayerBinding? = null
    private lateinit var musicListPA: ArrayList<Music>
    private var isPlaying: Boolean = false
    var musicService: MusicService? = null
    private  var index: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)

        val myClass = intent.getStringExtra("class")
        index = intent.getIntExtra("index", 0)

        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE )
        startService(intent)


//
//        println("mujeeb ka class $myClass and its index $index")

//        play pause button
        binding!!.playPause.setOnClickListener {
            if (isPlaying) {
                pause()
            }else {
                play()
            }
        }

        // next button
        binding!!.next.setOnClickListener {

            // if current music is last in list
            if(index == musicListPA.size - 1) {
                index = 0
            }else {
               index++
            }
            createMediaPlayer(index)
        }

        // next button
        binding!!.previous.setOnClickListener {
            if(index == 0) {
                index = musicListPA.size - 1
            }else {
                index--
            }
            createMediaPlayer(index)
        }

        when(myClass) {
            "HomeActivity" -> {
                musicListPA = MusicList
                setMusicData(index)


            }
        }
    }

    private fun createMediaPlayer(index: Int) {
        try {
            if(musicService!!.mediaPlayer == null) {
                musicService!!.mediaPlayer = MediaPlayer()
            }


            isPlaying = true
            setMusicData(index)
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[index].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            binding!!.playPause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
        }catch (e: java.lang.Exception) {
            return
        }
    }

    private fun play() {
        isPlaying = true
        binding!!.playPause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
        musicService!!.mediaPlayer!!.start()
    }
    private fun pause() {
        isPlaying = false
        binding!!.playPause.setImageResource(R.drawable.ic_baseline_play_circle_24)
        musicService!!.mediaPlayer!!.pause()
    }

    private val defaultColor: Int get() = ContextCompat.getColor(
        this,
        R.color.dark_purple
    )

    private fun setMusicData(index: Int) {

        val path = musicListPA[index].url
        val uri = Uri.parse(path)

        try {
            setBackgroundColor(uri)
        }catch (e: Exception) {
           binding!!.albumArt.setBackgroundResource(R.drawable.ic_baseline_music_note_24)
            Toast.makeText(this, "album art not found", Toast.LENGTH_SHORT).show()
        }

        binding!!.title.text = musicListPA[index].title
        binding!!.artist.text = musicListPA[index].artist
        binding!!.end.text = musicListPA[index].duration

        // album art loading
        Glide.with(this)
            .load(path)
            .into(binding!!.albumArt)

    }

    private fun setBackgroundColor(uri: Uri?) {
        val inputStream = this.contentResolver.openInputStream(uri!!)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val pallet = createPaletteSync(bitmap)
        binding!!.playerBg.apply {
            val color1 = pallet.getDarkVibrantColor(defaultColor)
            val color2 = pallet.getLightVibrantColor(defaultColor)
            val color3 = pallet.getMutedColor(defaultColor)

            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.BL_TR,
                intArrayOf(
                    color1,
                    color2,
                    color3
                )
            )

            background = gradientDrawable
        }
    }


    // Generate palette synchronously and return it
    fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()


    override fun onDestroy() {
        super.onDestroy()
        if (binding!=null) {
            binding = null
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer(index)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }




}