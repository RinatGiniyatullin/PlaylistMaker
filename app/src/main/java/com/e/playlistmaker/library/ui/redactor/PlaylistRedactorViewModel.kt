package com.e.playlistmaker.library.ui.redactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.e.playlistmaker.library.domain.newPlaylist.NewPlaylistInteractor
import com.e.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.e.playlistmaker.library.ui.newPlaylist.NewPlaylistViewModel
import kotlinx.coroutines.launch

class PlaylistRedactorViewModel(
    interactor: PlaylistInteractor,
    newPlaylistInteractor: NewPlaylistInteractor,
) : NewPlaylistViewModel(interactor, newPlaylistInteractor) {

    private val _back = MutableLiveData<Boolean>()
    val back: LiveData<Boolean> = _back

    fun updatePlaylistInfo(
        loadUri: String,
        title: String,
        description: String,
        playlistId: Int,
    ) {
        viewModelScope.launch {
            interactor.updatePlaylistInfo(playlistId, loadUri, title, description)
                .collect { result ->
                    _back.postValue(result)
                }
        }
    }
}