package com.example.mmplayer.home

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mmplayer.adapter.MusicAdapter
import com.example.mmplayer.databinding.FragmentHomeBinding
import com.example.mmplayer.modle.Music
import java.io.File
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        var list = listOfMusic()
        val adapter = MusicAdapter(requireContext(), list)
        binding!!.homeRc.adapter = adapter

        return binding!!.root


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

        val cursor = requireContext().contentResolver.query(
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