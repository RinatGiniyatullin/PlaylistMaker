package com.e.playlistmaker.search.presentation

import com.e.playlistmaker.search.domain.Track


interface SearchScreenView {
    fun showHistory(historyTracks: List<Track>)
    fun clearSearchText()
    fun hideKeyboard()
    fun showEmptyResult()
    fun showTracks(tracks: List<Track>)
    fun showTracksError()
    fun showLoading()
    fun updateHistoryTracks(historyTracks: MutableList<Track>)
}