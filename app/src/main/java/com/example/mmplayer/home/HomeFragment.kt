package com.example.mmplayer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mmplayer.MainActivity.Companion.MusicList
import com.example.mmplayer.adapter.MusicAdapter
import com.example.mmplayer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        var list = MusicList
        val adapter = MusicAdapter(requireContext(), list)
        binding!!.homeRc.adapter = adapter

        return binding!!.root


    }


    override fun onDestroy() {
        super.onDestroy()
        if (binding!=null) {
            binding = null
        }
    }

}