package com.e.playlistmaker.search.domain

interface SearchRepository {
    var tracksLoadResultListener: TracksLoadResultListener?

    fun loadTracks(query: String)
}