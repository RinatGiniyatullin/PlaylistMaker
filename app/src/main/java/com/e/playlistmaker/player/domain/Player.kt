package com.e.playlistmaker.player.domain

import com.e.playlistmaker.player.ui.PlayerState

interface Player {
    var playerState: PlayerState
    fun play()
    fun pause()
    fun releasePlayer()
    fun getCurrentPosition(): Int

}