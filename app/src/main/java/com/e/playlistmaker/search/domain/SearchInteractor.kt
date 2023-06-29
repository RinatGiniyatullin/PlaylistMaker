package com.e.playlistmaker.search.domain

interface SearchInteractor{
    fun clearHistory()
    fun getHistory(): List<Track>
    suspend fun loadTracks(query: String)
    fun writeHistory(historyTracks: List<Track>)
    fun unsubscribeFromTracksLoadResult()
    fun subscribeOnTracksLoadResult(listener: TracksLoadResultListener)
}