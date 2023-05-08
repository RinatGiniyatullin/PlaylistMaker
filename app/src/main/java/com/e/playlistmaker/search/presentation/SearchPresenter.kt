package com.e.playlistmaker.search.presentation

import com.e.playlistmaker.search.domain.ISearchInteractor
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.domain.TracksLoadResultListener

class SearchPresenter(
    private val view: SearchScreenView,
    private val interactor: ISearchInteractor,
    private val router: Router,
) {

    private val cachedTracks = mutableListOf<Track>()
    private val historyTracks = mutableListOf<Track>()

    init {
        interactor.subscribeOnTracksLoadResult(object : TracksLoadResultListener {
            override fun onSuccess(tracks: List<Track>) {
                if (tracks.isEmpty()) {
                    view.showEmptyResult()
                } else {
                    view.showTracks(tracks)
                }
            }

            override fun onError() {
                view.showTracksError()
            }
        })

        historyTracks.addAll(interactor.getHistory())
    }

    fun onHistoryClearClicked() {
        interactor.clearHistory()
        view.showHistory(interactor.getHistory())
    }

    fun searchFocusChanged(hasFocus: Boolean, text: String) {
        val historyTracks = interactor.getHistory()
        if (hasFocus && text.isEmpty() && historyTracks.isNotEmpty()) {
            view.showHistory(historyTracks)
        } else {
            view.showTracks(cachedTracks)
        }
    }

    fun searchTextClearClicked() {
        view.clearSearchText()
        view.hideKeyboard()
        view.showHistory(interactor.getHistory())
    }

    fun loadTracks(query: String) {
        if (query.isEmpty()) {
            return
        }
        view.showLoading()
        interactor.loadTracks(
            query = query
        )
    }

    fun backButtonClicked() {
        router.goBack()
    }

    fun onTrackClicked(track: Track) {
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
        router.openTrack(track.trackId)
    }

    fun onHistoryTrackClicked(track: Track) {
        historyTracks.remove(track)
        historyTracks.add(0, track)
        interactor.writeHistory(historyTracks)
        view.updateHistoryTracks(historyTracks)
        router.openTrack(track.trackId)
    }

    fun onDestroyView() {
        interactor.unsubscribeFromTracksLoadResult()
    }
}