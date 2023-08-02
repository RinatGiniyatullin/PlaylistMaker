package com.e.playlistmaker.library.data.db.favoriteTracks

import com.e.playlistmaker.library.data.db.AppDatabase
import com.e.playlistmaker.library.data.db.entity.TrackEntity
import com.e.playlistmaker.library.domain.favoriteTracks.FavoriteTracksRepository
import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksRepositoryImpl(private val database: AppDatabase) : FavoriteTracksRepository {
    override suspend fun addToFavorite(track: Track) {
        database.trackDao().insertTrack(mapToEntityTracks(track))
    }

    override suspend fun deleteFromFavorite(track: Track) {
        database.trackDao().deleteTrack(mapToEntityTracks(track))
    }

    override suspend fun getFavoriteTracks(): Flow<List<Track>> {
        val tracksFlow = database.trackDao().getFavoriteTracks()
        return tracksFlow.map { list -> list.map { mapToTracks(it)} }
    }

    private fun mapToEntityTracks(track: Track): TrackEntity {
        return TrackEntity(
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