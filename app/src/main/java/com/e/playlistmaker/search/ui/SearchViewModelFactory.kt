package com.e.playlistmaker.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.e.playlistmaker.search.domain.SearchInteractor

class SearchViewModelFactory(private val interactor: SearchInteractor) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(interactor) as T
    }
}