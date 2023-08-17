package com.e.playlistmaker.library.ui.listPlaylists

import com.e.playlistmaker.library.domain.Playlist

sealed class ListPlaylistsState {


    class Playlists(val playlists: List<Playlist>) : ListPlaylistsState()
    object Empty : ListPlaylistsState()
}