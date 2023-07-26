package com.e.playlistmaker.player.ui

import com.e.playlistmaker.library.domain.Playlist

sealed class AddResultState {
    class PositiveResult(val playlist: Playlist) : AddResultState()
    class NegativeResult(val playlist: Playlist) : AddResultState()
}