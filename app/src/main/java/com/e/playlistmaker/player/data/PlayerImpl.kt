package com.e.playlistmaker.player.data

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.e.playlistmaker.player.domain.Player
import com.e.playlistmaker.player.ui.PlayerState
import com.e.playlistmaker.search.data.HistorySearchDataStoreImpl

class PlayerImpl(private val trackId: String, private val sharedPrefHistory: SharedPreferences) : Player {
    override var playerState = PlayerState.STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()

    init {
        val track = HistorySearchDataStoreImpl(sharedPrefHistory).getHistory().firstOrNull { track ->
            track.trackId == trackId
        }
        mediaPlayer.apply {
            setDataSource(track?.previewUrl)
            setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED
            }
        }
    }

    override fun play() {
        if (playerState == PlayerState.STATE_DEFAULT) {
            mediaPlayer.prepare()
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}