package com.e.playlistmaker.library.data.db

import com.e.playlistmaker.library.data.db.entity.PlaylistEntity
import com.e.playlistmaker.library.data.db.entity.TrackEntity
import com.e.playlistmaker.library.data.db.entity.TrackForPlaylistEntity
import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.library.domain.PlaylistRepository
import com.e.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(private val database: AppDatabase) : PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        database.playlistDao().insertPlaylist(mapToEntityPlaylist(playlist))
    }

    override suspend fun addNewTrack(track: Track, playlist: Playlist) {
        //обновить список ид трэков плейлиста
        playlist.tracksId.add(track.trackId)

        //увеличить счётчик количества треков
        playlist.numberOfTracks += 1

        //обновить запись изменённого плейлиста в базе данных
        database.playlistDao().updatePlaylist(mapToEntityPlaylist(playlist))

        //добавляем трек в БД для плейлистов
        database.tracksForPlaylistDao().insertTrackInPlaylist(mapToEntityTracks(track))

    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        val playlistFlow = database.playlistDao().getPlaylists()
        return playlistFlow.map { list -> list.map { mapToPlaylist(it) } }
    }

    private fun mapToEntityPlaylist(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            title = playlist.title,
            description = playlist.description,
            uriForImage = playlist.uriForImage.toString(),
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
        val listTracksId = Gson().fromJson<MutableList<String>>(tracksId, myType).toMutableList()
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

    private fun mapToTracks(trackEntity: TrackEntity): Track {
        return Track(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTimeMillis = trackEntity.trackTimeMillis,
            artworkUrl100 = trackEntity.artworkUrl100,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            previewUrl = trackEntity.previewUrl,
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country
        )
    }
}