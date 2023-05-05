package com.e.playlistmaker.player.domain

import com.e.playlistmaker.search.domain.IHistorySearchDataStore
import com.e.playlistmaker.search.domain.Track

class PlayerInteractor(
    private val historySearchDataStore: IHistorySearchDataStore,
) : IPlayerInteractor {

    override fun loadTrack(trackId: String): Track? {
        return historySearchDataStore.getHistory().firstOrNull { track ->
            track.trackId == trackId
        }
    }
}