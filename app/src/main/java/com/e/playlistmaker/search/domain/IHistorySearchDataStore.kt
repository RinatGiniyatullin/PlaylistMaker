package com.e.playlistmaker.search.domain

interface IHistorySearchDataStore {
    fun clearHistory()

    fun getHistory(): List<Track>

    fun writeHistory(tracks: List<Track>)
}