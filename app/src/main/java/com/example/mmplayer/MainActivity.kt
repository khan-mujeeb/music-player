package com.example.mmplayer

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mmplayer.databinding.ActivityMainBinding
import com.example.mmplayer.modle.Music
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var MusicList: ArrayList<Music>
    }

    private var binding: ActivityMainBinding? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        requestSoragePermissions()
        MusicList = listOfMusic()
        MusicList.add(
            Music(
                "","","","","","",""
            )
        )
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        init()
        val c = Calendar.getInstance()

        when(c.get(Calendar.HOUR_OF_DAY)) {
            in 5..12 -> {
                setGrettings("\uD83C\uDF05 Good Morning...")
            }
            in 12..16 -> {
                setGrettings("☀️Good AfterNoon...")
            }
            else ->{
                setGrettings("\uD83C\uDF03 Good Evening...")
            }
        }

    }

    private fun setGrettings(s: String) {
        binding!!.wish.text = s
    }

    private fun requestSoragePermissions() {
        if(ActivityCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 13)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }else {
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 13)
        }
    }


    fun init() {
        val navControl = this.findNavController(R.id.nav_host_fragment)
        binding!!.bottomNavigation.setupWithNavController(navControl)

    }

    @SuppressLint("Range")
    private fun listOfMusic(): ArrayList<Music> {
        val tempList = ArrayList<Music>()

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            MediaStore.Audio.Media.DATE_ADDED + " DESC",
            null
        )

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val idColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                    val titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                    val albumColumnsIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
                    val artistColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                    val pathColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
                    val durationColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                    val AlbumArt = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUri = Uri.withAppendedPath(uri, AlbumArt).toString()

                    val durationInMilis = cursor.getLong(durationColumnIndex)
                    val min = TimeUnit.MINUTES.convert(durationInMilis, TimeUnit.MILLISECONDS)
                    val sec = TimeUnit.SECONDS.convert(durationInMilis, TimeUnit.MILLISECONDS) -
                            min * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
                    val duration = String.format("%02d:%02d", min, sec)



                    if (idColumnIndex >= 0 && titleColumnIndex >= 0) {
                        val music = Music(
                            id = cursor.getString(idColumnIndex),
                            title = cursor.getString(titleColumnIndex),
                            album = cursor.getString(albumColumnsIndex),
                            artist = cursor.getString(artistColumnIndex),
                            path = cursor.getString(pathColumnIndex),
                            duration = duration,
                            url = artUri
                        )

                        val file = File(music.path)
                        if (file.exists()) {
                            if (durationInMilis.toInt() > 30000)
                                tempList.add(music)
                        }
                    }
                }while (cursor.moveToNext())
                cursor.close()
            }
        }

        return tempList
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding!=null) {
            binding = null
        }
    }
}