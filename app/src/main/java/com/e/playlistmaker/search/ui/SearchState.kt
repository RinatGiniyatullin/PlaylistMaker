package com.e.playlistmaker.search.ui

import com.e.playlistmaker.search.domain.Track

sealed class SearchState {

    object Loading : SearchState()
    class History(val tracks: List<Track>, val clearText:Boolean = false) : SearchState()
    class Tracks(val tracks: List<Track>) : SearchState()
    object Error : SearchState()
    object Empty : SearchState()
}
