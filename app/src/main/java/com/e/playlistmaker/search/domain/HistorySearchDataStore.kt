package com.e.playlistmaker.search.domain

interface HistorySearchDataStore {
    fun clearHistory()

    fun getHistory(): List<Track>

    fun writeHistory(tracks: List<Track>)
}