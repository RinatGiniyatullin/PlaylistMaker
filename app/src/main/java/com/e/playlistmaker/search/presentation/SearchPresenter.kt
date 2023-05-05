package com.e.playlistmaker.search.presentation

import com.e.playlistmaker.search.domain.ISearchInteractor
import com.e.playlistmaker.search.domain.Track

class SearchPresenter(
    private val view: SearchScreenView,
    private val interactor: ISearchInteractor,
    private val router: SearchRouter,
) {

    private val cachedTracks = mutableListOf<Track>()
    private val historyTracks = mutableListOf<Track>()

    init {
        view.showHistory(historyTracks)
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
        interactor.loadTracks(query = query,
            onSuccess = { tracks ->
                cachedTracks.clear()
                cachedTracks.addAll(tracks)
                if (tracks.isEmpty()) {
                    view.showEmptyResult()
                } else {
                    view.showTracks(tracks)
                }
            }, onError = {
                view.showTracksError()
            })
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
        if (historyTracks.size == 10) {
            historyTracks.removeAt(9)
        }
        interactor.writeHistory(historyTracks)

        view.showHistory(historyTracks)
        router.openTrack(track.trackId)
    }

    fun onHistoryTrackClicked(track: Track) {
        router.openTrack(track.trackId)
    }
}