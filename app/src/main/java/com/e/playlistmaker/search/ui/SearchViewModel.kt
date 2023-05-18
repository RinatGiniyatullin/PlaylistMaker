package com.e.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.playlistmaker.search.domain.SearchInteractor
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.domain.TracksLoadResultListener


class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel() {

    private val historyTracks = mutableListOf<Track>()

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> = _state

    init {
        interactor.subscribeOnTracksLoadResult(object : TracksLoadResultListener {
            override fun onSuccess(tracks: List<Track>) {
                if (tracks.isEmpty()) {
                    _state.postValue(SearchState.Empty)
                } else {
                    _state.postValue(SearchState.Tracks(tracks))
                }
            }

            override fun onError() {
                _state.postValue(SearchState.Error)
            }
        })

        historyTracks.addAll(interactor.getHistory())
    }

    fun searchFocusChanged(hasFocus: Boolean, text: String) {
        val historyTracks = interactor.getHistory()
        if (hasFocus && text.isEmpty() && historyTracks.isNotEmpty()) {
            _state.postValue(SearchState.History(historyTracks))
        }
    }

    fun loadTracks(query: String) {
        if (query.isEmpty()) {
            return
        }
        _state.postValue(SearchState.Loading)
        interactor.loadTracks(
            query = query
        )
    }

    fun openHistoryTrack(track: Track) {
        historyTracks.remove(track)
        historyTracks.add(0, track)
        interactor.writeHistory(historyTracks)
        _state.postValue(SearchState.History(historyTracks))
    }

    fun onDestroyView() {
        interactor.unsubscribeFromTracksLoadResult()
    }

    fun clearHistory() {
        interactor.clearHistory()
        _state.postValue(SearchState.History(interactor.getHistory()))
    }

    fun clearSearchText() {
        _state.postValue(SearchState.History(interactor.getHistory(), true))
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