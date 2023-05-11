package com.e.playlistmaker.search.domain

interface ISearchInteractor{
    fun clearHistory()
    fun getHistory(): List<Track>
    fun loadTracks(query: String)
    fun writeHistory(historyTracks: List<Track>)
    fun unsubscribeFromTracksLoadResult()
    fun subscribeOnTracksLoadResult(listener: TracksLoadResultListener)
}