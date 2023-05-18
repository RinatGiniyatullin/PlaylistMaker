package com.e.playlistmaker.settings.data

import com.e.playlistmaker.settings.domain.ThemeSettings

interface SettingsDataStorage {
    fun saveTheme(settings: ThemeSettings)
    fun getTheme():Boolean
}