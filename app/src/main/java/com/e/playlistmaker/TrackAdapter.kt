package com.e.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class TrackAdapter :
    RecyclerView.Adapter<TrackViewHolder>() {

    var audio = ArrayList<ITunesAudio>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(audio.get(position))
    }

    override fun getItemCount() = audio.size

}

