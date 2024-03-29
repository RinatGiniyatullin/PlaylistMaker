package com.e.playlistmaker

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.e.playlistmaker.di.dataModule
import com.e.playlistmaker.di.interactorModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.e.playlistmaker.di.repositoryModule
import com.e.playlistmaker.di.viewModelModule
import com.markodevcic.peko.PermissionRequester

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        startKoin {
            androidContext(this@App)
            modules(repositoryModule, dataModule, interactorModule, viewModelModule)
        }

        val sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        val darkTheme = sharedPrefs.getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkTheme)

        PermissionRequester.initialize(applicationContext)
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val PREFERENCES = "Preferences"
        const val DARK_THEME_KEY = "Dark_theme_key"
        const val TRACK = "Track"
        lateinit var appContext: Context
    }
}