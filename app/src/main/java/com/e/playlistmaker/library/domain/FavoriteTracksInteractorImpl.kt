package com.e.playlistmaker.library.domain

import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksInteractorImpl(private val repository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {
    override suspend fun addToFavorite(track: Track) {
        repository.addToFavorite(track)
    }

    override suspend fun deleteFromFavorite(track: Track) {
        repository.deleteFromFavorite(track)
    }

    override suspend fun getFavoriteTracks(): Flow<List<Track>> {
        return repository.getFavoriteTracks()
            .map { list -> list.reversed() }
    }
}