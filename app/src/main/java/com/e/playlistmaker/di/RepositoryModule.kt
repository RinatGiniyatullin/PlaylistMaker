package com.e.playlistmaker.di

import com.e.playlistmaker.library.data.db.favoriteTracks.FavoriteTracksRepositoryImpl
import com.e.playlistmaker.library.domain.favoriteTracks.FavoriteTracksRepository
import com.e.playlistmaker.library.domain.playlist.PlaylistRepository
import com.e.playlistmaker.library.data.db.playlist.PlaylistRepositoryImpl
import com.e.playlistmaker.library.domain.newPlaylist.NewPlaylistRepository
import com.e.playlistmaker.library.data.newPlaylist.NewPlaylistRepositoryImpl
import com.e.playlistmaker.search.data.SearchRepositoryImpl
import com.e.playlistmaker.search.domain.SearchRepository
import com.e.playlistmaker.settings.data.SettingsRepositoryImpl
import com.e.playlistmaker.settings.domain.SettingsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::SearchRepositoryImpl).bind<SearchRepository>()

    singleOf(::SettingsRepositoryImpl).bind<SettingsRepository>()

    singleOf(::FavoriteTracksRepositoryImpl).bind<FavoriteTracksRepository>()

    singleOf(::PlaylistRepositoryImpl).bind<PlaylistRepository>()

    singleOf(::NewPlaylistRepositoryImpl).bind<NewPlaylistRepository>()

}