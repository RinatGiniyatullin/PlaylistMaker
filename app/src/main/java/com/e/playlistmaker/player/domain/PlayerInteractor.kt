package com.e.playlistmaker.player.domain

import com.e.playlistmaker.player.presentation.PlayerState
import com.e.playlistmaker.search.domain.IHistorySearchDataStore
import com.e.playlistmaker.search.domain.Track

class PlayerInteractor(
    private val historySearchDataStore: IHistorySearchDataStore,
    private val player: IPlayer,
) : IPlayerInteractor {

    override fun loadTrack(trackId: String): Track? {
        return historySearchDataStore.getHistory().firstOrNull { track ->
            track.trackId == trackId
        }
    }

    override fun playTrack() {
        player.play()
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