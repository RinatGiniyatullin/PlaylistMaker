package com.e.playlistmaker.player.domain

interface Player {
    var playerState: PlayerState
    fun play(previewUrl: String)
    fun pause()
    fun releasePlayer()
    fun getCurrentPosition(): Int

}