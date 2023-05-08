package com.e.playlistmaker.player.domain

import com.e.playlistmaker.player.presentation.PlayerState

interface IPlayer {
    var playerState: PlayerState
    fun play()
    fun pause()
    fun releasePlayer()
    fun getCurrentPosition(): Int

}