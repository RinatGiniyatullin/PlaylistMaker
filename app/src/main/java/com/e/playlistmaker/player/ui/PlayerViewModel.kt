package com.e.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.playlistmaker.library.domain.FavoriteTracksInteractor
import com.e.playlistmaker.player.domain.PlayerInteractor
import com.e.playlistmaker.player.domain.PlayerState
import com.e.playlistmaker.player.ui.DateTimeFormatter.PROGRESS_FORMAT
import com.e.playlistmaker.search.domain.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val interactor: PlayerInteractor,
    private val favoriteInteractor: FavoriteTracksInteractor,
) :
    ViewModel() {

    private var timerJob: Job? = null

    private var _showTrackLiveData = MutableLiveData<Track>()
    val showTrackLiveData: LiveData<Track> = _showTrackLiveData

    private var _playButtonStateLiveData = MutableLiveData<ButtonState>()
    val playButtonStateLiveData: LiveData<ButtonState> = _playButtonStateLiveData

    private var _likeButtonStateLiveData = MutableLiveData<ButtonState>()
    val likeButtonStateLiveData: LiveData<ButtonState> = _likeButtonStateLiveData

    private var _timeLiveData = MutableLiveData<String>()
    val timeLiveData: LiveData<String> = _timeLiveData

    override fun onCleared() {
        super.onCleared()
        interactor.stopPlayer()
    }

    private fun startPlayer(previewUrl: String) {
        _playButtonStateLiveData.postValue(ButtonState.Pause)
        interactor.playTrack(previewUrl)
        startTimer()

    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (interactor.getPlayerState() == PlayerState.STATE_PLAYING) {
                delay(TIMER_DELAY)
                _timeLiveData.postValue(getFormat(interactor.getPlayerTime()))
            }
            if (interactor.getPlayerState() == PlayerState.STATE_PREPARED) {
                _timeLiveData.postValue(getFormat(START_TIME))
                _playButtonStateLiveData.postValue(ButtonState.Play)
            }
        }
    }

    private fun getFormat(currentTime: Int): String {
        return SimpleDateFormat(PROGRESS_FORMAT, Locale.getDefault()).format(currentTime)
    }

    private fun pausePlayer() {
        interactor.pauseTrack()
        timerJob?.cancel()
        _playButtonStateLiveData.postValue(ButtonState.Play)
    }

    fun loadTrack(trackId: String) {
        viewModelScope.launch {
          interactor.loadTrack(trackId).collect{_showTrackLiveData.postValue(it)}
    }
}

    fun playTrack(trackId: String) {
        when (interactor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            else -> {
                viewModelScope.launch { interactor.loadTrack(trackId).collect{startPlayer(it.previewUrl)} }
               /* val track = interactor.loadTrack(trackId)!!
                startPlayer(track.previewUrl)*/
            }
        }
    }

    fun pause() {
        pausePlayer()
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite == false) {
                _likeButtonStateLiveData.postValue(ButtonState.Like)
                favoriteInteractor.addToFavorite(track)
            } else {
                _likeButtonStateLiveData.postValue(ButtonState.DisLike)
                favoriteInteractor.deleteFromFavorite(track)
            }
        }
    }

    companion object {
        private const val TIMER_DELAY = 300L
        private const val START_TIME = 0
    }
}