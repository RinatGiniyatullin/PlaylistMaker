package com.e.playlistmaker.search.domain

interface SearchRepository {
    var tracksLoadResultListener: TracksLoadResultListener?

    suspend fun loadTracks(query: String)
}