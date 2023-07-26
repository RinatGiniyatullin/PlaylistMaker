package com.e.playlistmaker.library.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.library.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {


    private val _state = MutableLiveData<PlaylistState>()
    val state: LiveData<PlaylistState> = _state

    fun showPlaylist() {
        viewModelScope.launch {
            interactor.getPlaylists()
                .collect { positiveResult(it) }

        }
    }

    private fun positiveResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _state.postValue(PlaylistState.Empty)
        } else {
            _state.postValue(PlaylistState.Playlists(playlists))
        }
    }
}