package com.e.playlistmaker.player.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.e.playlistmaker.R
import com.e.playlistmaker.player.domain.IAudioPlayer
import com.e.playlistmaker.player.domain.PlayerInteractor
import com.e.playlistmaker.player.domain.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer(private val interactor: PlayerInteractor) : IAudioPlayer {

    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())

    private val run = object : Runnable {
        override fun run() {
            if (playerState == PlayerState.STATE_PLAYING)
                interactor.setProgressTimeText(SimpleDateFormat(PROGRESS_FORMAT, Locale.getDefault()).format(mediaPlayer.currentPosition))

            handler.postDelayed(this, DELAY)
        }
    }

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            interactor.setEnable(true)
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            interactor.setImageButton(R.drawable.play)
            playerState = PlayerState.STATE_PREPARED
            handler.removeCallbacks(run)
            interactor.setProgressTimeText(DEFAULT_MM_SS)
        }
    }

    override fun playButtonClicked() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> startPlayer()
        }
    }

    companion object {
        private const val DELAY = 300L
        private const val DEFAULT_MM_SS = "00:00"
        private const val PROGRESS_FORMAT = "mm:ss"
    }
}