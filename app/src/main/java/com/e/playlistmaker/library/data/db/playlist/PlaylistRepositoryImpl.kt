package com.e.playlistmaker.library.data.db.playlist

import com.e.playlistmaker.library.data.db.AppDatabase
import com.e.playlistmaker.library.data.db.entity.PlaylistEntity
import com.e.playlistmaker.library.data.db.entity.TrackForPlaylistEntity
import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.library.domain.playlist.PlaylistRepository
import com.e.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(private val database: AppDatabase) : PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        database.playlistDao().insertPlaylist(mapToEntityPlaylist(playlist))
    }

    override suspend fun addNewTrack(track: Track, playlist: Playlist) {
        playlist.tracksId.add(track.trackId)

        playlist.numberOfTracks += 1

        database.playlistDao().updatePlaylist(mapToEntityPlaylist(playlist))

        database.tracksForPlaylistDao().insertTrackInPlaylist(mapToEntityTracks(track))

    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        val playlistFlow = database.playlistDao().getPlaylists()
        return playlistFlow.map { list -> list.map { mapToPlaylist(it) } }
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return mapToPlaylist(database.playlistDao().getPlaylistById(playlistId))
    }

    override suspend fun getTracksForPlaylist(tracksId: List<String>): List<Track> {
        val allTracks = mutableListOf<Track>()
        val list = database.tracksForPlaylistDao().getTracksForPlaylists()
            .map { list -> mapToTracks(list) }
        for (track in list) {
            if (tracksId.contains(track.trackId)) {
                allTracks.add(track)
            }
        }
        return allTracks
    }

    override fun deleteTrack(trackId: String, playlistId: Int): Flow<Int> = flow {
        val playlist = mapToPlaylist(database.playlistDao().getPlaylistById(playlistId))
        playlist.tracksId.remove(trackId)
        playlist.numberOfTracks -= 1
        database.playlistDao().updatePlaylist(mapToEntityPlaylist(playlist))
        checkTracksForPlaylistDB(listOf(trackId))
        emit(playlistId)
    }

    override fun deletePlaylist(playlistId: Int): Flow<Boolean> = flow {

        val playlist = mapToPlaylist(database.playlistDao().getPlaylistById(playlistId))
        val listTrackId = playlist.tracksId
        database.playlistDao().deletePlaylist(playlistId)
        checkTracksForPlaylistDB(listTrackId)
        emit(true)
    }

    override fun updatePlaylistInfo(
        playlistId: Int,
        uri: String,
        title: String,
        description: String,
    ): Flow<Boolean> = flow {
        val playlist = mapToPlaylist(database.playlistDao().getPlaylistById(playlistId))
        val newPlaylist = Playlist(
            playlistId, title, description, uri, playlist.tracksId, playlist.numberOfTracks
        )
        database.playlistDao().updatePlaylist(mapToEntityPlaylist(newPlaylist))
        emit(true)
    }

    private suspend fun checkTracksForPlaylistDB(listTrackId: List<String>) {

        val set = mutableSetOf<String>()
        val listPlaylists = database.playlistDao().getPlaylists().firstOrNull()
            ?.let { it.map { mapToPlaylist(it) } }
        if (listPlaylists != null) {
            for (playlist in listPlaylists) {
                for (listTracks in playlist.tracksId) {
                    set.addAll(listOf(listTracks))
                }
            }
            for (trackId in listTrackId) {
                if (set.contains(trackId)) {
                } else {
                    database.tracksForPlaylistDao().deleteTrack(trackId)
                }
            }
        } else {
            return
        }
    }

    private fun mapToEntityPlaylist(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            title = playlist.title,
            description = playlist.description,
            uriForImage = playlist.uriForImage,
            tracksId = createStringFromList(playlist.tracksId),
            numberOfTracks = playlist.numberOfTracks
        )
    }

    private fun createStringFromList(tracksId: MutableList<String>): String {
        val json = Gson().toJson(tracksId)
        return json
    }


    private fun mapToPlaylist(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlistEntity.playlistId,
            title = playlistEntity.title,
            description = playlistEntity.description,
            uriForImage = playlistEntity.uriForImage,
            tracksId = createListFromString(playlistEntity.tracksId),
            numberOfTracks = playlistEntity.numberOfTracks
        )
    }

    private fun createListFromString(tracksId: String): MutableList<String> {
        val myType = object : TypeToken<MutableList<String>>() {}.type
        val listTracksId =
            Gson().fromJson<MutableList<String>>(tracksId, myType).toMutableList()
        return listTracksId
    }

    private fun mapToEntityTracks(track: Track): TrackForPlaylistEntity {
        return TrackForPlaylistEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            previewUrl = track.previewUrl,
            primaryGenreName = track.primaryGenreName,
            country = track.country
        )
    }

    private fun mapToTracks(trackForPlaylistEntity: TrackForPlaylistEntity): Track {
        return Track(
            trackId = trackForPlaylistEntity.trackId,
            trackName = trackForPlaylistEntity.trackName,
            artistName = trackForPlaylistEntity.artistName,
            trackTimeMillis = trackForPlaylistEntity.trackTimeMillis,
            artworkUrl100 = trackForPlaylistEntity.artworkUrl100,
            collectionName = trackForPlaylistEntity.collectionName,
            releaseDate = trackForPlaylistEntity.releaseDate,
            previewUrl = trackForPlaylistEntity.previewUrl,
            primaryGenreName = trackForPlaylistEntity.primaryGenreName,
            country = trackForPlaylistEntity.country
        )
    }
}