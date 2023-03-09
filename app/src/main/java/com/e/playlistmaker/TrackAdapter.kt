package com.e.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var itemClickListener: ((ITunesAudio) -> Unit)? = null
    var audio = ArrayList<ITunesAudio>()
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

