package com.e.playlistmaker.library.domain.playlist

import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {


    suspend fun createPlaylist(playlist: Playlist)

    suspend fun addNewTrack(track: Track, playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

}