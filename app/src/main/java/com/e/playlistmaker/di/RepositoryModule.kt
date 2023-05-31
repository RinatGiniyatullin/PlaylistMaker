package com.e.playlistmaker.di

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

}