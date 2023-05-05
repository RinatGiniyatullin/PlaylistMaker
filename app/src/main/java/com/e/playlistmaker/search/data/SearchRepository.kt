package com.e.playlistmaker.search.data

import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.domain.ISearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository(private val api: ITunesApi) : ISearchRepository {

    override fun loadTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit) {

        api.search(query)
            .enqueue(object : Callback<ITunesResponse> {
                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>,
                ) {
                    if (response.code() == 200) {
                        onSuccess.invoke(response.body()?.results!!.map { mapTrack(it) })
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {}
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
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl
        )
    }
}