package com.e.playlistmaker.library.domain.playlist

import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun createPlaylist(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }

    override suspend fun addNewTrack(track: Track, playlist: Playlist) {
        repository.addNewTrack(track, playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }
}