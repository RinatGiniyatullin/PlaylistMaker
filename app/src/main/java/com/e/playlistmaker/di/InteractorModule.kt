package com.e.playlistmaker.di

import com.e.playlistmaker.library.domain.favoriteTracks.FavoriteTracksInteractor
import com.e.playlistmaker.library.domain.favoriteTracks.FavoriteTracksInteractorImpl
import com.e.playlistmaker.library.domain.newPlaylist.NewPlaylistInteractor
import com.e.playlistmaker.library.domain.newPlaylist.NewPlaylistInteractorImpl
import com.e.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.e.playlistmaker.library.domain.playlist.PlaylistInteractorImpl
import com.e.playlistmaker.player.domain.PlayerInteractor
import com.e.playlistmaker.player.domain.PlayerInteractorImpl
import com.e.playlistmaker.search.domain.SearchInteractor
import com.e.playlistmaker.search.domain.SearchInteractorImpl
import com.e.playlistmaker.settings.domain.SettingsInteractor
import com.e.playlistmaker.settings.domain.SettingsInteractorImpl
import com.e.playlistmaker.sharing.domain.SharingInteractor
import com.e.playlistmaker.sharing.domain.SharingInteractorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {

    singleOf(::SearchInteractorImpl).bind<SearchInteractor>()

    singleOf(::PlayerInteractorImpl).bind<PlayerInteractor>()

    singleOf(::SettingsInteractorImpl).bind<SettingsInteractor>()

    singleOf(::SharingInteractorImpl).bind<SharingInteractor>()

    singleOf(::FavoriteTracksInteractorImpl).bind<FavoriteTracksInteractor>()

    singleOf(::PlaylistInteractorImpl).bind<PlaylistInteractor>()

    singleOf(::NewPlaylistInteractorImpl).bind<NewPlaylistInteractor>()

}