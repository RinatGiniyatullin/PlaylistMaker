package com.e.playlistmaker.library.domain.playlist

import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun addNewTrack(track: Track, playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun getPlaylistById(playlistId: Int): Playlist
    suspend fun getTracksForPlaylist(tracksId: List<String>): List<Track>
    fun deleteTrack(trackId: String, playlistId: Int): Flow<Int>
    fun deletePlaylist(playlistId: Int): Flow<Boolean>
    fun updatePlaylistInfo(
        playlistId: Int,
        uri: String,
        title: String,
        description: String,
    ): Flow<Boolean>

}