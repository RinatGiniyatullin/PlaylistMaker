package com.e.playlistmaker.library.ui.listPlaylists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e.playlistmaker.databinding.PlaylistItemBinding
import com.e.playlistmaker.library.domain.Playlist

class ListPlaylistsAdapter :
    RecyclerView.Adapter<ListPlaylistsViewHolder>() {

    var itemClickListener: ((Playlist) -> Unit)? = null
    var playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlaylistsViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)

        return ListPlaylistsViewHolder(PlaylistItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: ListPlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { itemClickListener?.invoke(playlists[position]) }

    }
}

