package com.e.playlistmaker.player.data

import android.media.MediaPlayer
import com.e.playlistmaker.player.domain.IPlayer
import com.e.playlistmaker.player.presentation.PlayerState

class Player(private val previewUrl: String) : IPlayer {
    override var playerState = PlayerState.STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()

    init {
        mediaPlayer.apply {
            setDataSource(previewUrl)
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