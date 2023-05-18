package com.e.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e.playlistmaker.databinding.TracksViewBinding
import com.e.playlistmaker.search.domain.Track

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var itemClickListener: ((Track) -> Unit)? = null
    var audio = mutableListOf<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)

        return TrackViewHolder(TracksViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = audio.get(position)
        holder.bind(track)
        holder.itemView.setOnClickListener { itemClickListener?.invoke(audio[position]) }
    }

    override fun getItemCount() = audio.size

}

