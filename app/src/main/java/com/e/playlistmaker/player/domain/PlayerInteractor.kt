package com.e.playlistmaker.player.domain

import com.e.playlistmaker.search.domain.Track

interface PlayerInteractor {

    fun loadTrack(trackId: String): Track?
    fun playTrack()
    fun pauseTrack()
    fun getPlayerState(): PlayerState
    fun getPlayerTime(): Int
    fun stopPlayer()

}