package com.example.mmplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mmplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding!=null) {
            binding = null
        }
    }
}