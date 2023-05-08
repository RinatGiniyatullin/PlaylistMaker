package com.e.playlistmaker.search.domain

interface ISearchRepository {
    var tracksLoadResultListener: TracksLoadResultListener?

    fun loadTracks(query: String)
}