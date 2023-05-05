package com.e.playlistmaker.player.presentation

import android.media.MediaPlayer
import android.os.Looper
import android.os.Handler
import com.e.playlistmaker.R
import com.e.playlistmaker.player.domain.IPlayerInteractor
import com.e.playlistmaker.search.domain.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerPresenter(
    private val view: PlayerScreenView,
    private val interactor: IPlayerInteractor,
) {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private val run = object : Runnable {
        override fun run() {
            if (playerState == PlayerState.STATE_PLAYING)
                view.setProgressTimeText(
                    SimpleDateFormat(
                        PROGRESS_FORMAT,
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                )
            handler.postDelayed(this, DELAY)
        }
    }

    fun backButtonClicked() {
        view.goBack()
    }

    fun playButtonClicked() {
        if (playerState == PlayerState.STATE_PLAYING) pausePlayer()
        if (playerState == PlayerState.STATE_PREPARED || playerState == PlayerState.STATE_PAUSED) startPlayer()
    }

    private fun startPlayer() {
        mediaPlayer.start()
        view.setImageButton(R.drawable.pause)
        playerState = PlayerState.STATE_PLAYING
        handler.post(run)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        view.setImageButton(R.drawable.play)
        playerState = PlayerState.STATE_PAUSED
        handler.removeCallbacks(run)
    }

    fun loadTrack(trackId: String) {
        val track = interactor.loadTrack(trackId)!!
        view.showTrackCover(track.getCoverArtwork())
        view.showTrackName(track.trackName)
        view.showArtistName(track.artistName)
        setVisibilityTrackTimeGroup(track)
        setVisibilityCollectionNameGroup(track)
        setVisibilityReleaseDateGroup(track)
        setVisibilityGenreNameGroup(track)
        setVisibilityCountryGroup(track)
        preparePlayer(track)
    }

    fun setVisibilityTrackTimeGroup(track: Track) {
        if (track.trackTimeMillis != 0L) {
            view.showTrackTimeText(
                SimpleDateFormat(PROGRESS_FORMAT, Locale.getDefault()).format(
                    track.trackTimeMillis
                )
            )
        } else {
            view.goneTrackTimeGroup()
        }
    }

    fun setVisibilityCollectionNameGroup(track: Track) {
        if (track.collectionName.isNotEmpty()) {
            view.showCollectionNameText(track.collectionName)
        } else {
            view.goneCollectionNameGroup()
        }
    }

    fun setVisibilityReleaseDateGroup(track: Track) {
        if (track.releaseDate.isNotEmpty()) {
            view.showReleaseDateText(track.getReleaseDateOnlyYear())
        } else {
            view.goneReleaseDateGroup()
        }
    }

    fun setVisibilityGenreNameGroup(track: Track) {
        if (track.primaryGenreName.isNotEmpty()) {
            view.showGenreNameText(track.primaryGenreName)
        } else {
            view.goneGenreNameGroup()
        }
    }

    fun setVisibilityCountryGroup(track: Track) {
        if (track.country.isNotEmpty()) {
            view.showCountryText(track.country)
        } else {
            view.goneCountryTextGroup()
        }
    }

    fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            view.setEnableButton(true)
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            view.setImageButton(R.drawable.play)
            playerState = PlayerState.STATE_PREPARED
            handler.removeCallbacks(run)
            view.setProgressTimeText(DEFAULT_MM_SS)
        }
    }

    fun playerPaused() {
        pausePlayer()
    }

    fun playerOnDestroyed() {
        mediaPlayer.release()
        handler.removeCallbacks(run)
    }

    companion object {
        private const val DELAY = 300L
        private const val DEFAULT_MM_SS = "00:00"
        private const val PROGRESS_FORMAT = "mm:ss"
    }

}