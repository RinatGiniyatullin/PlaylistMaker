package com.e.playlistmaker.search.data

import com.e.playlistmaker.library.data.db.AppDatabase
import com.e.playlistmaker.search.data.dto.TrackDto
import com.e.playlistmaker.search.data.network.ITunesApi
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.domain.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepositoryImpl(private val api: ITunesApi, private val database: AppDatabase) : SearchRepository {

    override suspend fun loadTracks(query: String): Flow<List<Track>> = flow {

        val response = api.search(query)

        response.apply {
            val idFavoriteTracks = database.trackDao().getIdFavoriteTracks()
            val tracks =
                response.results.map { mapTrack(it) }.filter { track ->
                    track.previewUrl != null
                }
            for (i in tracks) {
                for (j in idFavoriteTracks) {
                    if (i.trackId.equals(j)){
                        i.isFavorite = true
                    }
                }
            }
            emit(tracks)
        }
    }.flowOn(Dispatchers.IO).catch { throw IllegalStateException() }

    private fun mapTrack(trackDto: TrackDto): Track {
        return Track(
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.trackId,
            trackDto.collectionName,
            trackDto.releaseDate.orEmpty(),
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl.orEmpty()
        )
    }
}