package com.e.playlistmaker.library.ui.playlist

import com.e.playlistmaker.library.domain.Playlist

sealed class PlaylistState {


    class Playlists(val playlists: List<Playlist>) : PlaylistState()
    object Empty : PlaylistState()
}