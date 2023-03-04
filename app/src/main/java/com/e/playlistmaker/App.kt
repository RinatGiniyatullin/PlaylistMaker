package com.e.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val SETTING_PREFERENCES = "Setting_preferences"
const val DARK_THEME_KEY = "Dark_theme_key"
const val HISTORY_PREFERENCES = "History_preferences"
const val TRACK_KEY = "Track_key"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}