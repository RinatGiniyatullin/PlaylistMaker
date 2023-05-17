package com.e.playlistmaker.settings.data

import com.e.playlistmaker.settings.domain.SettingsRepository
import com.e.playlistmaker.settings.domain.ThemeSettings

class SettingsRepositoryImpl(private val settingsDataStorage: SettingsDataStorage) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
       return ThemeSettings(darkTheme = settingsDataStorage.getTheme())
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsDataStorage.saveTheme(settings)
    }
}
