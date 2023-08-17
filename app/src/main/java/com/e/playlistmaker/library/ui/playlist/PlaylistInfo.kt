package com.e.playlistmaker.library.ui.playlist

import com.e.playlistmaker.search.domain.Track

sealed class PlaylistInfo {

    class Info(
        val cover: String,
        val title: String,
        val description: String,
        val time: String,
        val tracksCount: String,
        val tracks: List<Track>,
        val visibilityForSuffix: Boolean,
    ) : PlaylistInfo()
}