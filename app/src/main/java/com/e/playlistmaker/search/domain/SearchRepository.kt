package com.e.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun loadTracks(query: String): Flow<List<Track>>
}