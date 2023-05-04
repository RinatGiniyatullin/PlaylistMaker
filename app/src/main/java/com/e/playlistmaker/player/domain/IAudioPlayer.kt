package com.e.playlistmaker.player.domain

interface IAudioPlayer {
    fun preparePlayer(url:String)
    fun playButtonClicked()
}