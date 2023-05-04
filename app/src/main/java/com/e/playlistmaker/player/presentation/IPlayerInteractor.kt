package com.e.playlistmaker.player.presentation

import com.e.playlistmaker.search.domain.Track

interface IPlayerInteractor {

    fun loadTrack(trackId: String): Track?
    fun preparePlayer(url:String)
    fun setEnable(b: Boolean)
    fun setImageButton(image: Int)
    fun setProgressTimeText(text: String)
    fun playButtonClicked()
}