package com.e.playlistmaker.search.domain


class SearchInteractor(
    private val historySearchDataStore: IHistorySearchDataStore,
    private val repository: ISearchRepository,
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

