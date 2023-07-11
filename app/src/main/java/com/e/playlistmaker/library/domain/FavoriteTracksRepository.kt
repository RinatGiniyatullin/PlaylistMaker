package com.e.playlistmaker.library.domain

import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addToFavorite(track: Track)

    suspend fun deleteFromFavorite(track: Track)

    suspend fun getFavoriteTracks(): Flow<List<Track>>

}