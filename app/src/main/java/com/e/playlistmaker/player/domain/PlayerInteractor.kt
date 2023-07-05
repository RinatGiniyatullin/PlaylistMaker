package com.e.playlistmaker.player.domain

import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {

    suspend fun loadTrack(trackId: String): Flow<Track>
    fun playTrack(previewUrl:String)
    fun pauseTrack()
    fun getPlayerState(): PlayerState
    fun getPlayerTime(): Int
    fun stopPlayer()

}