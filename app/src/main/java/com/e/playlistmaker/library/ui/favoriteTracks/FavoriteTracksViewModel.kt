package com.e.playlistmaker.library.ui.favoriteTracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.playlistmaker.library.domain.FavoriteTracksInteractor
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val favoriteInteractor: FavoriteTracksInteractor) :
    ViewModel() {

    private var _favoriteLiveData = MutableLiveData<FavoriteState>()
    val favoriteLiveData: LiveData<FavoriteState> = _favoriteLiveData

    init {
        viewModelScope.launch {

            favoriteInteractor.getFavoriteTracks().collect { list ->
                if (list.isEmpty()) {
                    _favoriteLiveData.postValue(FavoriteState.Empty)
                } else {
                    _favoriteLiveData.postValue(FavoriteState.FavoriteTracks(list))
                }
            }
        }
    }
}