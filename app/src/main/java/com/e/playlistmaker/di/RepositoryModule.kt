package com.e.playlistmaker.di

import com.e.playlistmaker.search.data.SearchRepositoryImpl
import com.e.playlistmaker.search.domain.SearchRepository
import com.e.playlistmaker.settings.data.SettingsRepositoryImpl
import com.e.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchRepository> {
        SearchRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}