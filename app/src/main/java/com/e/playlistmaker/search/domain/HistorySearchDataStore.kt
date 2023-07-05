package com.e.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface HistorySearchDataStore {
    fun clearHistory()

    fun getHistory(): Flow<List<Track>>

    fun writeHistory(tracks: List<Track>)
}