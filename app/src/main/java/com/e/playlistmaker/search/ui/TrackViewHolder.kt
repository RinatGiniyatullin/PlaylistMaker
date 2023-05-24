package com.e.playlistmaker.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.TracksViewBinding
import com.e.playlistmaker.player.ui.DateTimeFormatter.PROGRESS_FORMAT
import com.e.playlistmaker.search.domain.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(private val binding: TracksViewBinding) : RecyclerView.ViewHolder(
binding.root) {

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.getCoverArtwork())
            .placeholder(R.drawable.cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(binding.trackCover)

        binding.trackName.text = model.trackName
        binding.artistName.text = model.artistName
        binding.trackTime.text =
            SimpleDateFormat(PROGRESS_FORMAT, Locale.getDefault()).format(model.trackTimeMillis)
    }
}


