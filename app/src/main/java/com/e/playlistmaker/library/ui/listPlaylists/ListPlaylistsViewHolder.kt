package com.e.playlistmaker.library.ui.listPlaylists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.PlaylistItemBinding
import com.e.playlistmaker.library.domain.Playlist

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
        binding.numberTracks.text = mapText(model.numberOfTracks)
    }

    private fun mapText(numberOfTracks: Int): String {
        return when (numberOfTracks) {
            11, 12, 13, 14, 15, 16, 17, 18, 19 -> "$numberOfTracks треков"
            else -> when (numberOfTracks % 10) {
                1 -> "$numberOfTracks трек"
                2, 3, 4 -> "$numberOfTracks трека"
                5, 6, 7, 8, 9, 0 -> "$numberOfTracks треков"
                else -> ""
            }
        }
    }
}


