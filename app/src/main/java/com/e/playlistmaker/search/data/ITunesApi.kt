package com.e.playlistmaker.search.data

import retrofit2.Call
import retrofit2.http.*

interface ITunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunesResponse>
}