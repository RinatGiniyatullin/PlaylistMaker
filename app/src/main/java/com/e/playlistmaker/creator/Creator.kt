package com.e.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.e.playlistmaker.HISTORY_PREFERENCES
import com.e.playlistmaker.SETTING_PREFERENCES
import com.e.playlistmaker.player.data.PlayerImpl
import com.e.playlistmaker.player.domain.Player
import com.e.playlistmaker.player.domain.PlayerInteractor
import com.e.playlistmaker.player.domain.PlayerInteractorImpl
import com.e.playlistmaker.search.data.HistorySearchDataStoreImpl
import com.e.playlistmaker.search.data.SearchRepositoryImpl
import com.e.playlistmaker.search.data.network.ITunesApi
import com.e.playlistmaker.search.domain.HistorySearchDataStore
import com.e.playlistmaker.search.domain.SearchInteractor
import com.e.playlistmaker.search.domain.SearchInteractorImpl
import com.e.playlistmaker.search.domain.SearchRepository
import com.e.playlistmaker.search.ui.SearchViewModelFactory
import com.e.playlistmaker.settings.data.SettingsDataStorage
import com.e.playlistmaker.settings.data.SettingsRepositoryImpl
import com.e.playlistmaker.settings.data.SharedPrefsSettingsDataStorage
import com.e.playlistmaker.settings.domain.SettingsInteractor
import com.e.playlistmaker.settings.domain.SettingsInteractorImpl
import com.e.playlistmaker.settings.domain.SettingsRepository
import com.e.playlistmaker.settings.ui.SettingsViewModelFactory
import com.e.playlistmaker.sharing.data.ExternalNavigator
import com.e.playlistmaker.sharing.domain.SharingInteractor
import com.e.playlistmaker.sharing.domain.SharingInteractorImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(
            getHistorySearchDataStore(context),
            getSearchRepository()
        )
    }

    private fun getHistorySharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(HISTORY_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }

    private fun getHistorySearchDataStore(context: Context): HistorySearchDataStore {
        return HistorySearchDataStoreImpl(getHistorySharedPreferences(context))
    }

    private fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(getITunesApi())
    }

    private fun getITunesApi(): ITunesApi {

        return Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ITunesApi::class.java)
    }

    fun provideSearchViewModelFactory(context: Context): SearchViewModelFactory {
        return SearchViewModelFactory(provideSearchInteractor(context))

    }

    fun provideSettingsViewModelFactory(
        externalNavigator: ExternalNavigator,
        context: Context,
    ): SettingsViewModelFactory {
        return SettingsViewModelFactory(
            provideSharingInteractor(externalNavigator),
            provideSettingsInteractor(context)
        )

    }

    private fun provideSharingInteractor(externalNavigator: ExternalNavigator): SharingInteractor {
        return SharingInteractorImpl(externalNavigator)
    }

    private fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(getSettingsDataStorage(context))
    }

    private fun getSettingsDataStorage(context: Context): SettingsDataStorage {
        return SharedPrefsSettingsDataStorage(getSharedPrefs(context))
    }

    private fun getSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(SETTING_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }

    fun providePlayerInteractor(context: Context, trackId: String): PlayerInteractor {
        return PlayerInteractorImpl(getHistorySearchDataStore(context), getPlayer(trackId, context))
    }

    private fun getPlayer(trackId: String, context: Context): Player {
        return PlayerImpl(trackId, getHistorySharedPreferences(context))
    }
}