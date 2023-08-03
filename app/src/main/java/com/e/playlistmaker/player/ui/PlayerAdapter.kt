package com.e.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e.playlistmaker.databinding.PlaylistItemForPlayerBinding
import com.e.playlistmaker.library.domain.Playlist

class PlayerAdapter : RecyclerView.Adapter<PlayerViewHolder>() {

    var itemClickListener: ((Playlist) -> Unit)? = null
    var playlists = mutableListOf<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)

        return PlayerViewHolder(
            PlaylistItemForPlayerBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val playlist = playlists.get(position)
        holder.bind(playlist)
        holder.itemView.setOnClickListener { itemClickListener?.invoke(playlists[position]) }
    }

    override fun getItemCount() = playlists.size

}