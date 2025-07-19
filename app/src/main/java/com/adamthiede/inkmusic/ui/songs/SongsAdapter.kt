package com.adamthiede.inkmusic.ui.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adamthiede.inkmusic.R
import com.adamthiede.inkmusic.model.*

class SongsAdapter(
    private var songs: List<Song>,
    private val onSongClick: (Int) -> Unit
) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.song_title)
        val artist: TextView = itemView.findViewById(R.id.song_artist)
    }

//    itemView.setOnClickListener {
//        viewModel.playSong(song)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.title.text = song.title
        holder.artist.text = song.artist
        holder.itemView.setOnClickListener { onSongClick(position) }
    }

    override fun getItemCount(): Int = songs.size

    fun setSongs(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }
}

