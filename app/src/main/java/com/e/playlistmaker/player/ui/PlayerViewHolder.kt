package com.e.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.PlaylistItemForPlayerBinding
import com.e.playlistmaker.library.domain.Playlist

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
        binding.playlistNumbers.text = mapText(model.numberOfTracks)
    }

    private fun mapText(numberOfTracks: Int): String {
        var text = ""
        when (numberOfTracks % 10) {
            1 -> text = "$numberOfTracks трек"
            2, 3, 4 -> text = "$numberOfTracks трека"
            5, 6, 7, 8, 9, 0 -> text = "$numberOfTracks треков"
        }
        return text
    }
}