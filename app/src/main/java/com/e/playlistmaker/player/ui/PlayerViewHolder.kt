package com.e.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.PlaylistItemForPlayerBinding
import com.e.playlistmaker.library.domain.Playlist
import createTracksCountString

class PlayerViewHolder(private val binding: PlaylistItemForPlayerBinding) : RecyclerView.ViewHolder(
    binding.root
) {

    fun bind(model: Playlist) {

        Glide.with(itemView)
            .load(model.uriForImage)
            .placeholder(R.drawable.cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(binding.playlistCover)

        binding.playlistName.text = model.title
        binding.playlistNumbers.text = createTracksCountString(model.numberOfTracks)
    }
}