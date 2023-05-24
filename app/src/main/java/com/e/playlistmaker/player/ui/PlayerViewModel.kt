package com.e.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.playlistmaker.player.domain.PlayerInteractor
import com.e.playlistmaker.player.domain.PlayerState
import com.e.playlistmaker.player.ui.DateTimeFormatter.PROGRESS_FORMAT
import com.e.playlistmaker.search.domain.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val interactor: PlayerInteractor) :
    ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var _showTrackLiveData = MutableLiveData<Track>()
    val showTrackLiveData: LiveData<Track> = _showTrackLiveData

    private var _buttonStateLiveData = MutableLiveData<ButtonState>()
    val buttonStateLiveData: LiveData<ButtonState> = _buttonStateLiveData

    private var _timeLiveData = MutableLiveData<String>()
    val timeLiveData: LiveData<String> = _timeLiveData

    override fun onCleared() {
        super.onCleared()
        interactor.stopPlayer()
        handler.removeCallbacksAndMessages(null)
    }

    private fun startPlayer(previewUrl: String) {
        _buttonStateLiveData.postValue(ButtonState.Pause)
        interactor.playTrack(previewUrl)
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (interactor.getPlayerState() == PlayerState.STATE_PREPARED) {
                    _timeLiveData.postValue(getFormat(TIME_START))
                    _buttonStateLiveData.postValue(ButtonState.Play)
                    handler.removeCallbacksAndMessages(null)
                } else {
                    _timeLiveData.postValue(getFormat(interactor.getPlayerTime()))
                    handler.postDelayed(this, DELAY)
                }
            }
        }, DELAY)
    }

    private fun getFormat(currentTime: Int): String {
        return SimpleDateFormat(PROGRESS_FORMAT, Locale.getDefault()).format(currentTime)
    }

    private fun pausePlayer() {
        interactor.pauseTrack()
        _buttonStateLiveData.postValue(ButtonState.Play)
        handler.removeCallbacksAndMessages(null)
    }

    fun loadTrack(trackId: String) {
        val track = interactor.loadTrack(trackId)!!
        _showTrackLiveData.postValue(track)
    }

    fun playTrack(trackId: String) {
        when (interactor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            else -> {
                val track = interactor.loadTrack(trackId)!!
                startPlayer(track.previewUrl)
            }
        }
    }

    fun pause() {
        pausePlayer()
    }

    companion object {
        private const val DELAY = 300L
        private const val TIME_START = 0
    }
}