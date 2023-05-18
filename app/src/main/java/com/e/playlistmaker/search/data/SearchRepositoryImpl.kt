package com.e.playlistmaker.search.data

import com.e.playlistmaker.search.data.dto.ITunesResponse
import com.e.playlistmaker.search.data.dto.TrackDto
import com.e.playlistmaker.search.data.network.ITunesApi
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.domain.SearchRepository
import com.e.playlistmaker.search.domain.TracksLoadResultListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepositoryImpl(private val api: ITunesApi) : SearchRepository {

    override var tracksLoadResultListener: TracksLoadResultListener? = null

    override fun loadTracks(query: String) {

        api.search(query)
            .enqueue(object : Callback<ITunesResponse> {
                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>,
                ) {
                    if (response.code() == 200) {
                        val tracks =
                            response.body()?.results!!.map { mapTrack(it) }.filter { track ->
                                track.previewUrl != null
                            }
                        tracksLoadResultListener?.onSuccess(tracks = tracks)

                    }
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    tracksLoadResultListener?.onError()
                }
            })
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