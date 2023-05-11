package com.e.playlistmaker.player.presentation

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

    private val handler = Handler(Looper.getMainLooper())

    fun backButtonClicked() {
        view.goBack()
    }

    fun playButtonClicked() {
        when (interactor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            else -> startPlayer()
        }
    }

    private fun startPlayer() {
        view.setImageButton(R.drawable.pause)
        interactor.playTrack()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (interactor.getPlayerState() == PlayerState.STATE_PREPARED) {
                    view.updadeProgressTime(TIME_START)
                    view.setImageButton(R.drawable.play)
                    handler.removeCallbacksAndMessages(null)
                } else {
                    view.updadeProgressTime(interactor.getPlayerTime())
                    handler.postDelayed(this, DELAY)
                }
            }
        }, DELAY)
    }

    private fun pausePlayer() {
        interactor.pauseTrack()
        view.setImageButton(R.drawable.play)
        handler.removeCallbacksAndMessages(null)
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
    }

    private fun setVisibilityTrackTimeGroup(track: Track) {
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

    private fun setVisibilityCollectionNameGroup(track: Track) {
        if (track.collectionName.isNotEmpty()) {
            view.showCollectionNameText(track.collectionName)
        } else {
            view.goneCollectionNameGroup()
        }
    }

    private fun setVisibilityReleaseDateGroup(track: Track) {
        if (track.releaseDate.isNotEmpty()) {
            view.showReleaseDateText(track.getReleaseDateOnlyYear())
        } else {
            view.goneReleaseDateGroup()
        }
    }

    private fun setVisibilityGenreNameGroup(track: Track) {
        if (track.primaryGenreName.isNotEmpty()) {
            view.showGenreNameText(track.primaryGenreName)
        } else {
            view.goneGenreNameGroup()
        }
    }

    private fun setVisibilityCountryGroup(track: Track) {
        if (track.country.isNotEmpty()) {
            view.showCountryText(track.country)
        } else {
            view.goneCountryTextGroup()
        }
    }

    fun playerPaused() {
        pausePlayer()
    }

    fun playerOnDestroyed() {
        interactor.stopPlayer()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val DELAY = 300L
        private const val PROGRESS_FORMAT = "mm:ss"
        private const val TIME_START = 0
    }
}