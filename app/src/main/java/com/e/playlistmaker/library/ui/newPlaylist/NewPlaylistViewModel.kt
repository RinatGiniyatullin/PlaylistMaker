package com.e.playlistmaker.library.ui.newPlaylist

import android.Manifest
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.library.domain.newPlaylist.NewPlaylistInteractor
import com.e.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val interactor: PlaylistInteractor,
    private val newPlaylistInteractor: NewPlaylistInteractor,
) : ViewModel() {
    val requester = PermissionRequester.instance()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private var loadUri: Uri? = null

    private var _playlistLiveData = MutableLiveData<List<Playlist>>()
    val playlistLiveData: LiveData<List<Playlist>> = _playlistLiveData

    private var _placeholderLiveData = MutableLiveData<Boolean>()
    val placeholderLiveData: LiveData<Boolean> = _placeholderLiveData

    private var _pictureLiveData = MutableLiveData<Uri?>()
    val pictureLiveData: LiveData<Uri?> = _pictureLiveData

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            if (loadUri != null) {
                newPlaylistInteractor.savePicture(loadUri!!, playlist.title)
            }
            interactor.createPlaylist(playlist)
            interactor.getPlaylists().collect { list ->
                _playlistLiveData.postValue(list)
            }
        }
    }

    fun onUiCreate(newPlaylistFragment: NewPlaylistFragment) {
        pickMedia =
            newPlaylistFragment.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                viewModelScope.launch {
                    if (uri != null) {
                        loadUri = uri
                        _placeholderLiveData.postValue(false)
                        _pictureLiveData.postValue(uri)
                    }
                }
            }
    }

    fun cover() {
        viewModelScope.launch {
            requester.request(Manifest.permission.CAMERA).collect { result ->
                when (result) {
                    is PermissionResult.Granted -> {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }

                    is PermissionResult.Denied.DeniedPermanently -> {}
                    is PermissionResult.Denied.NeedsRationale -> {}
                    is PermissionResult.Cancelled -> {
                        return@collect
                    }
                }
            }
        }
    }
}
