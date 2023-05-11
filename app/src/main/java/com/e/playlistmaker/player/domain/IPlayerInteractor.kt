package com.e.playlistmaker.player.domain

import com.e.playlistmaker.player.presentation.PlayerState
import com.e.playlistmaker.search.domain.Track

interface IPlayerInteractor {

    fun loadTrack(trackId: String): Track?
    fun playTrack()
    fun pauseTrack()
    fun getPlayerState(): PlayerState
    fun getPlayerTime(): Int
    fun stopPlayer()

}