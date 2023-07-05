package com.e.playlistmaker.library.domain

import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Collections

class FavoriteTracksInteractorImpl(private val repository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {
    override suspend fun addToFavorite(track: Track) {
        repository.addToFavorite(track)
    }

    override suspend fun deleteFromFavorite(track: Track) {
        repository.deleteFromFavorite(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        repository.getFavoriteTracks().map { list -> Collections.reverse(list) }
    }

}