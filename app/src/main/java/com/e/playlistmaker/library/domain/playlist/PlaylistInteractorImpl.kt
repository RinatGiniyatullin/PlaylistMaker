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

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return repository.getPlaylistById(playlistId)
    }

    override suspend fun getTracksForPlaylist(tracksId: List<String>): List<Track> {
        return repository.getTracksForPlaylist((tracksId)).reversed()
    }

    override fun deleteTrack(trackId: String, playlistId: Int): Flow<Int> {
        return repository.deleteTrack(trackId, playlistId)
    }

    override fun deletePlaylist(playlistId: Int): Flow<Boolean> {
        return repository.deletePlaylist(playlistId)
    }

    override fun updatePlaylistInfo(
        playlistId: Int,
        uri: String,
        title: String,
        description: String,
    ): Flow<Boolean> {
        return repository.updatePlaylistInfo(
            playlistId,
            uri,
            title,
            description
        )
    }
}