package com.e.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.e.playlistmaker.App.Companion.PREFERENCES
import com.e.playlistmaker.player.data.PlayerImpl
import com.e.playlistmaker.player.domain.Player
import com.e.playlistmaker.search.data.HistorySearchDataStoreImpl
import com.e.playlistmaker.search.data.network.ITunesApi
import com.e.playlistmaker.search.domain.HistorySearchDataStore
import com.e.playlistmaker.settings.data.SettingsDataStorage
import com.e.playlistmaker.settings.data.SharedPrefsSettingsDataStorage
import com.e.playlistmaker.sharing.data.ExternalNavigator
import com.e.playlistmaker.sharing.data.ExternalNavigatorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ITunesApi::class.java)
    }

    single {
        androidContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    }

    singleOf(::HistorySearchDataStoreImpl).bind<HistorySearchDataStore>()

    single<Player> {
        PlayerImpl(MediaPlayer())
    }

    singleOf(::SharedPrefsSettingsDataStorage).bind<SettingsDataStorage>()

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }


}