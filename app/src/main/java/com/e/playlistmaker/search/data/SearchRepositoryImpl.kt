package com.e.playlistmaker.search.data

import com.e.playlistmaker.search.data.dto.TrackDto
import com.e.playlistmaker.search.data.network.ITunesApi
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.domain.SearchRepository
import com.e.playlistmaker.search.domain.TracksLoadResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(private val api: ITunesApi) : SearchRepository {

    override var tracksLoadResultListener: TracksLoadResultListener? = null

    override suspend fun loadTracks(query: String) {

        withContext(Dispatchers.IO) {
            val response = api.search(query)
            try {
                response.apply {
                    val tracks =
                        response.results!!.map { mapTrack(it) }.filter { track ->
                            track.previewUrl != null
                        }
                    tracksLoadResultListener?.onSuccess(tracks = tracks)
                }
            } catch (e: Throwable) {
                tracksLoadResultListener?.onError()
            }
        }
    }

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