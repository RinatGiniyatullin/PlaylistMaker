package com.e.playlistmaker.di

import com.e.playlistmaker.player.domain.PlayerInteractor
import com.e.playlistmaker.player.domain.PlayerInteractorImpl
import com.e.playlistmaker.search.domain.SearchInteractor
import com.e.playlistmaker.search.domain.SearchInteractorImpl
import com.e.playlistmaker.settings.domain.SettingsInteractor
import com.e.playlistmaker.settings.domain.SettingsInteractorImpl
import com.e.playlistmaker.sharing.domain.SharingInteractor
import com.e.playlistmaker.sharing.domain.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<SearchInteractor> {
        SearchInteractorImpl(get(), get())
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}