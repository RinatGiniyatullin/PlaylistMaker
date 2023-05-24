package com.e.playlistmaker.player.domain

import com.e.playlistmaker.search.domain.HistorySearchDataStore
import com.e.playlistmaker.search.domain.Track

class PlayerInteractorImpl(
    private val historySearchDataStore: HistorySearchDataStore,
    private val player: Player,
) : PlayerInteractor {

    override fun loadTrack(trackId: String): Track? {
        return historySearchDataStore.getHistory().firstOrNull { track ->
            track.trackId == trackId
        }
    }

    override fun playTrack(previewUrl:String) {
        player.play(previewUrl)
    }

    override fun pauseTrack() {
        player.pause()
    }

    override fun getPlayerState(): PlayerState {
        return player.playerState
    }

    override fun getPlayerTime(): Int {
        return player.getCurrentPosition()
    }

    override fun stopPlayer() {
        player.releasePlayer()
    }
}