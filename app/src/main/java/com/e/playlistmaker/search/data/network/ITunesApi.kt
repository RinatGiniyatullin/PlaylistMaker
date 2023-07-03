package com.e.playlistmaker.search.data.network

import com.e.playlistmaker.search.data.dto.ITunesResponse
import retrofit2.http.*

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): ITunesResponse
}