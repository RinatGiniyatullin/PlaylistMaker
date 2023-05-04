package com.e.playlistmaker.player.presentation

import com.e.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerPresenter(
    private val view: PlayerScreenView,
    private val interactor: IPlayerInteractor,
    private val router: AudioPlayerRouter,
) {

    fun backButtonClicked() {
        router.goBack()
    }

    fun playButtonClicked() {
        interactor.playButtonClicked()
        when (playerState) {
            AudioPlayerActivity.STATE_PLAYING -> pausePlayer()
            AudioPlayerActivity.STATE_PREPARED, AudioPlayerActivity.STATE_PAUSED -> startPlayer()
        }
    }

    fun loadTrack(trackId: String) {
        val track = interactor.loadTrack(trackId)!!
        view.showTrackCover(track.getCoverArtwork())
    }

    fun showTrackName(trackId: String) {
        val track = interactor.loadTrack(trackId)!!
        view.showTrackName(track.trackName)
    }

    fun showArtistName(trackId: String) {
        val track = interactor.loadTrack(trackId)!!
        view.showArtistName(track.artistName)
    }

    fun setVisibilityTrackTimeGroup(trackId: String) {
        val track = interactor.loadTrack(trackId)!!
        if (track.trackTimeMillis != 0L) {
            view.showTrackTimeText(
                SimpleDateFormat(PROGRESS_FORMAT, Locale.getDefault()).format(
                    track.trackTimeMillis
                )
            )
        } else {
            view.hideTrackTimeGroup()
        }
    }

    fun setVisibilityCollectionNameGroup(trackId: String) {
        val track = interactor.loadTrack(trackId)!!
        if (track.collectionName.isNotEmpty()) {
            view.showCollectionNameText(track.collectionName)
        } else {
            view.hideCollectionNameGroup()
        }
    }

    fun setVisibilityReleaseDateGroup(trackId: String) {
        val track = interactor.loadTrack(trackId)!!
        if (track.releaseDate.isNotEmpty()) {
            view.showReleaseDateText(track.getReleaseDateOnlyYear())
        } else {
            view.hideReleaseDateGroup()
        }
    }

    fun setVisibilityGenreNameGroup(trackId: String) {
        val track = interactor.loadTrack(trackId)!!
        if (track.primaryGenreName.isNotEmpty()) {
            view.showGenreNameText(track.primaryGenreName)
        } else {
            view.hideGenreNameGroup()
        }
    }

    fun setVisibilityCountryGroup(trackId: String) {
        val track = interactor.loadTrack(trackId)!!
        if (track.country.isNotEmpty()) {
            view.showCountryText(track.country)
        } else {
            view.hideCountryTextGroup()
        }
    }

    fun preparePlayer(trackId: String) {
        val track = interactor.loadTrack(trackId)!!
        interactor.preparePlayer(track.previewUrl)

//        mediaPlayer.setDataSource(track.previewUrl)
//        mediaPlayer.prepareAsync()
//        mediaPlayer.setOnPreparedListener {
//            playButton.isEnabled = true
//            playerState = STATE_PREPARED
//        }
//        mediaPlayer.setOnCompletionListener {
//            playButton.setImageResource(R.drawable.play)
//            playerState = STATE_PREPARED
//            handler.removeCallbacks(run)
//            progressTime.text = AudioPlayerActivity.DEFAULT_MM_SS
//        }
    }

    companion object {
        private const val DELAY = 300L
        private const val DEFAULT_MM_SS = "00:00"
        private const val PROGRESS_FORMAT = "mm:ss"
    }

}