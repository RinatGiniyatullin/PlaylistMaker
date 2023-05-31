package com.e.playlistmaker.library.ui.favoriteTracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteTracksViewModel : ViewModel() {

    private var _liveData = MutableLiveData<String>()
    val liveData: LiveData<String> = _liveData
}