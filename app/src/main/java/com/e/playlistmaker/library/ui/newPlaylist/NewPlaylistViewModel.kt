package com.e.playlistmaker.library.ui.newPlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.library.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    private var _playlistLiveData = MutableLiveData<List<Playlist>>()
    val playlistLiveData: LiveData<List<Playlist>> = _playlistLiveData

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            interactor.createPlaylist(playlist)
            interactor.getPlaylists().collect { list ->
                _playlistLiveData.postValue(list)
            }
        }
    }
}
