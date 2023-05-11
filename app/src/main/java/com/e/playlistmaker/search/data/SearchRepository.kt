package com.e.playlistmaker.search.data

import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.domain.ISearchRepository
import com.e.playlistmaker.search.domain.TracksLoadResultListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository(private val api: ITunesApi) : ISearchRepository {

    override var tracksLoadResultListener: TracksLoadResultListener? = null

    override fun loadTracks(query: String) {

        api.search(query)
            .enqueue(object : Callback<ITunesResponse> {
                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>,
                ) {
                    if (response.code() == 200) {
                        //        onSuccess.invoke(response.body()?.results!!.map { mapTrack(it) })

                        val tracks =
                            response.body()?.results!!.map { mapTrack(it) }.filter { track ->
                                track.previewUrl != null
                            }
                        tracksLoadResultListener?.onSuccess(tracks = tracks)

                    }
                    //else {
                    //  onError.invoke()
                    //}
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
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl
        )
    }
}