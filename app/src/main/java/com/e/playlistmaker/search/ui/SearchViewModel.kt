package com.e.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.playlistmaker.search.domain.SearchInteractor
import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext


class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel() {

    private val historyTracks = mutableListOf<Track>()

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> = _state

    init {
        loadHistory()
        //historyTracks.addAll(interactor.getHistory())
    }

    fun searchFocusChanged(hasFocus: Boolean, text: String) {
        //   val historyTracks = interactor.getHistory()
        val historyTracks = loadHistory()
        if (hasFocus && text.isEmpty() && historyTracks.isNotEmpty()) {
            _state.postValue(SearchState.History(historyTracks))
        }
    }

    fun loadHistory(): List<Track> {
        val historyTracks = mutableListOf<Track>()
        viewModelScope.launch {
            try {
                interactor.getHistory()
                    .collect { list -> historyTracks.addAll(list) }
            } catch (e: Exception) {
                historyTracks.clear()
            }
        }
        return historyTracks
    }

    fun loadTracks(query: String) {
        if (query.isEmpty()) {
            return
        }
        _state.postValue(SearchState.Loading)

        viewModelScope.launch {
            try {
                interactor.loadTracks(query = query)
                    .collect { positiveResult(it) }
            } catch (e: Exception) {
                errorResult()
            }
        }
    }

    private fun positiveResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            _state.postValue(SearchState.Empty)
        } else {
            _state.postValue(SearchState.Tracks(tracks))
        }
    }

    private fun errorResult() {
        _state.postValue(SearchState.Error)
    }

    fun openHistoryTrack(track: Track) {
        historyTracks.remove(track)
        historyTracks.add(0, track)
        interactor.writeHistory(historyTracks)
        _state.postValue(SearchState.History(historyTracks))
    }

    fun clearHistory() {
        interactor.clearHistory()
        _state.postValue(SearchState.History(loadHistory()))
    }

    fun clearSearchText() {
        _state.postValue(SearchState.History(loadHistory(), true))
    }

    fun openTrack(track: Track) {
        if (historyTracks.contains(track)) {
            historyTracks.remove(track)
            historyTracks.add(0, track)
        } else {
            historyTracks.add(0, track)
        }
        if (historyTracks.size == 11) {
            historyTracks.removeAt(10)
        }
        interactor.writeHistory(historyTracks)
    }
}