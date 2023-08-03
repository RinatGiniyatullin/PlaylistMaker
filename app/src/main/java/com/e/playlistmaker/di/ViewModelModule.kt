package com.e.playlistmaker.di

import com.e.playlistmaker.library.ui.favoriteTracks.FavoriteTracksViewModel
import com.e.playlistmaker.library.ui.playlist.PlaylistViewModel
import com.e.playlistmaker.library.ui.newPlaylist.NewPlaylistViewModel
import com.e.playlistmaker.player.ui.PlayerViewModel
import com.e.playlistmaker.search.ui.SearchViewModel
import com.e.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::SearchViewModel)
    viewModelOf(::PlayerViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::FavoriteTracksViewModel)
    viewModelOf(::PlaylistViewModel)
    viewModelOf(::NewPlaylistViewModel)
}