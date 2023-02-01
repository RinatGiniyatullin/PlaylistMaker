package com.e.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context).inflate(R.layout.tracks_view, parentView, false)
) {
    private val trackCoverView = itemView.findViewById<ImageView>(R.id.trackCover)
    private val trackNameView = itemView.findViewById<TextView>(R.id.trackName)
    private val artistNameView = itemView.findViewById<TextView>(R.id.artistName)
    private val trackTimeView = itemView.findViewById<TextView>(R.id.trackTime)

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.close)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(trackCoverView)

        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTime
    }
}


