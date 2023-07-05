package com.e.playlistmaker.library.domain

import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun addToFavorite(track: Track)

    suspend fun deleteFromFavorite(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>
}