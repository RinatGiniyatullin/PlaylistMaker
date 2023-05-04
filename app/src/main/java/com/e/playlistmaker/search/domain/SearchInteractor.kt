package com.e.playlistmaker.search.domain

import com.e.playlistmaker.search.data.HistorySearchDataStore
import com.e.playlistmaker.search.data.SearchRepository

class SearchInteractor(
    private val historySearchDataStore: HistorySearchDataStore,
    private val repository: SearchRepository,
) : ISearchInteractor{
   override fun clearHistory() {
        historySearchDataStore.clearHistory()
    }

   override fun getHistory(): List<Track> {
        return historySearchDataStore.getHistory()
    }

   override fun loadTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit) {
        repository.loadTracks(query, onSuccess, onError)
    }

    override fun writeHistory(historyTracks: List<Track>) {
        historySearchDataStore.writeHistory(historyTracks)
    }
}

