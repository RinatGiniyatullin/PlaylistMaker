package com.e.playlistmaker.library.ui.listPlaylists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.PlaylistItemBinding
import com.e.playlistmaker.library.domain.Playlist
import createTracksCountString

class ListPlaylistsViewHolder(private val binding: PlaylistItemBinding) : RecyclerView.ViewHolder(
    binding.root
) {

    fun bind(model: Playlist) {
        Glide.with(itemView)
            .load(model.uriForImage)
            .placeholder(R.drawable.cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.image)

        binding.title.text = model.title
        binding.numberTracks.text = createTracksCountString(model.numberOfTracks)
    }
}


