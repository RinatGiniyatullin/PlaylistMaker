package com.e.playlistmaker.player.data

import android.media.MediaPlayer
import com.e.playlistmaker.player.domain.Player
import com.e.playlistmaker.player.domain.PlayerState

class PlayerImpl(private val mediaPlayer: MediaPlayer) : Player {
    override var playerState = PlayerState.STATE_DEFAULT

    override fun play(previewUrl: String) {
        when (playerState) {
            PlayerState.STATE_DEFAULT -> {
                mediaPlayer.apply {
                    reset()
                    setDataSource(previewUrl)
                    prepare()
                    setOnCompletionListener {
                        playerState = PlayerState.STATE_PREPARED
                    }
                    playerState = PlayerState.STATE_PREPARED
                    mediaPlayer.start()
                    playerState = PlayerState.STATE_PLAYING
                }
            }

            else -> {
                mediaPlayer.start()
                playerState = PlayerState.STATE_PLAYING
            }
        }
    }

    override fun pause() {
        if (playerState == PlayerState.STATE_PLAYING) {
            mediaPlayer.pause()
            playerState = PlayerState.STATE_PAUSED
        }
    }

    override fun releasePlayer() {
        mediaPlayer.apply {
            stop()
            reset()
            setOnCompletionListener(null)
        }
        playerState = PlayerState.STATE_DEFAULT
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}