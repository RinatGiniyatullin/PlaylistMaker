package com.e.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun clearHistory()
    fun getHistory(): Flow<List<Track>>
    suspend fun loadTracks(query: String): Flow<List<Track>>
    fun writeHistory(historyTracks: List<Track>)
}