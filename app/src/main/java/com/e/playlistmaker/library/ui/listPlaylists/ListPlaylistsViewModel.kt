package com.e.playlistmaker.library.ui.listPlaylists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.library.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class ListPlaylistsViewModel(private val interactor: PlaylistInteractor) : ViewModel() {


    private val _state = MutableLiveData<ListPlaylistsState>()
    val state: LiveData<ListPlaylistsState> = _state

    fun showPlaylist() {
        viewModelScope.launch {
            interactor.getPlaylists()
                .collect { positiveResult(it) }
        }
    }

    private fun positiveResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _state.postValue(ListPlaylistsState.Empty)
        } else {
            _state.postValue(ListPlaylistsState.Playlists(playlists))
        }
    }
}