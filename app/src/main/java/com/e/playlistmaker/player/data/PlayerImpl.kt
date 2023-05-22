package com.e.playlistmaker.player.data

import android.media.MediaPlayer
import com.e.playlistmaker.player.domain.Player
import com.e.playlistmaker.player.domain.PlayerState

class PlayerImpl : Player {
    override var playerState = PlayerState.STATE_DEFAULT
    private var mediaPlayer: MediaPlayer? = null

    override fun play(previewUrl: String) {
        if (playerState == PlayerState.STATE_DEFAULT) {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.apply {
                setDataSource(previewUrl)
            }

            mediaPlayer?.prepare()
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer?.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer?.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer?.apply {
            stop()
            reset()
            release()
        }
        mediaPlayer = null
        playerState = PlayerState.STATE_DEFAULT
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
}