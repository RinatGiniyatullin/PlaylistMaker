package com.e.playlistmaker.player.domain

import com.e.playlistmaker.search.domain.Track

interface IPlayerInteractor {

    fun loadTrack(trackId: String): Track?
}