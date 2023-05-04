package com.e.playlistmaker.search.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e.playlistmaker.search.domain.Track

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var itemClickListener: ((Track) -> Unit)? = null
    var audio = mutableListOf<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = audio.get(position)
        holder.bind(track)
        holder.itemView.setOnClickListener { itemClickListener?.invoke(audio[position]) }
    }

    override fun getItemCount() = audio.size

}

