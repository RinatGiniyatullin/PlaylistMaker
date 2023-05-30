package com.e.playlistmaker.di

import com.e.playlistmaker.library.ui.favoriteTracks.FavoriteTracksViewModel
import com.e.playlistmaker.library.ui.playlist.PlaylistViewModel
import com.e.playlistmaker.player.ui.PlayerViewModel
import com.e.playlistmaker.search.ui.SearchViewModel
import com.e.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        PlayerViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        FavoriteTracksViewModel()
    }

    viewModel {
        PlaylistViewModel()
    }
}