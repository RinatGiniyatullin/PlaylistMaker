package com.e.playlistmaker.library.domain

import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun addNewTrack(track: Track, playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

}