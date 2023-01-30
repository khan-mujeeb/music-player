package com.example.mmplayer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mmplayer.databinding.MusicLtemViewBinding
import com.example.mmplayer.modle.Music
import com.example.mmplayer.ui.PlayerActivity

class MusicAdapter(private val context: Context, private val list: ArrayList<Music>): RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    class MusicViewHolder(binding: MusicLtemViewBinding): RecyclerView.ViewHolder(binding.root) {
        val musicName = binding.musicName
        val artistName = binding.artistName
        val length = binding.length
        val albumArt = binding.albumArtList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(MusicLtemViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.musicName.text = list[position].title
        holder.artistName.text = list[position].artist
        holder.length.text = list[position].duration
        // album art loading
        Glide.with(context)
            .load(list[position].url)
            .into(holder.albumArt)

        // open player activity
        holder.itemView.setOnClickListener{
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("index", position)
            intent.putExtra("class", "HomeActivity")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}