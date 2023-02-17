package com.e.playlistmaker

import retrofit2.Call
import retrofit2.http.*

interface ITunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunesResponse>
}