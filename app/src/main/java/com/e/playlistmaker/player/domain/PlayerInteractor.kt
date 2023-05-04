package com.e.playlistmaker.player.domain

import com.e.playlistmaker.player.presentation.IPlayerInteractor
import com.e.playlistmaker.player.presentation.PlayerScreenView
import com.e.playlistmaker.search.domain.IHistorySearchDataStore
import com.e.playlistmaker.search.domain.Track

class PlayerInteractor(
    private val historySearchDataStore: IHistorySearchDataStore,
    private val audioPlayer: IAudioPlayer,
    private val view: PlayerScreenView
) : IPlayerInteractor {

    override fun loadTrack(trackId: String): Track? {
        return historySearchDataStore.getHistory().firstOrNull { track ->
            track.trackId == trackId
        }
    }

    override fun preparePlayer(url:String) {
        audioPlayer.preparePlayer(url)
    }

    override fun setEnable(b: Boolean) {
        view.setEnableButton(b)
    }

    override fun setImageButton(image: Int) {
        view.setImageButton(image)
    }

    override fun setProgressTimeText(text: String) {
        view.setProgressTimeText(text)
    }

    override fun playButtonClicked() {
        audioPlayer.playButtonClicked()
    }
}