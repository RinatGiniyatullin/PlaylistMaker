package com.e.playlistmaker.settings.data

import android.content.SharedPreferences
import com.e.playlistmaker.App.Companion.DARK_THEME_KEY
import com.e.playlistmaker.settings.domain.ThemeSettings

class SharedPrefsSettingsDataStorage(private val sharedPrefs: SharedPreferences) :
    SettingsDataStorage {

    override fun saveTheme(settings: ThemeSettings) {
        sharedPrefs.edit().putBoolean(DARK_THEME_KEY, settings.darkTheme).apply()
    }

    override fun getTheme():Boolean {
      return sharedPrefs.getBoolean(DARK_THEME_KEY, false)
    }
}

