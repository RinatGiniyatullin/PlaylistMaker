package com.e.playlistmaker.player.domain

import com.e.playlistmaker.search.domain.Track

interface PlayerInteractor {

    suspend fun loadTrack(trackId: String): Track
    fun playTrack(previewUrl:String)
    fun pauseTrack()
    fun getPlayerState(): PlayerState
    fun getPlayerTime(): Int
    fun stopPlayer()

}