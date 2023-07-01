package com.e.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow


class SearchInteractorImpl(
    private val historySearchDataStore: HistorySearchDataStore,
    private val repository: SearchRepository,
) : SearchInteractor {
    override fun clearHistory() {
        historySearchDataStore.clearHistory()
    }

    override fun getHistory(): List<Track> {
        return historySearchDataStore.getHistory()
    }

    override suspend fun loadTracks(query: String): Flow<List<Track>> {
        return repository.loadTracks(query)
    }

    override fun writeHistory(historyTracks: List<Track>) {
        historySearchDataStore.writeHistory(historyTracks)
    }
}

