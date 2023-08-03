package com.e.playlistmaker.library.ui.playlist

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.PlaylistItemBinding
import com.e.playlistmaker.library.domain.Playlist

class PlaylistViewHolder(private val binding: PlaylistItemBinding) : RecyclerView.ViewHolder(
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
        var text = ""
        when (numberOfTracks % 10) {
            1 -> text = "$numberOfTracks трек"
            2, 3, 4 -> text = "$numberOfTracks трека"
            5, 6, 7, 8, 9, 0 -> text = "$numberOfTracks треков"
        }
        return text
    }
}


