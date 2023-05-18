package com.e.playlistmaker.settings.domain

interface SettingsRepository {

    fun getThemeSettings(): ThemeSettings

    fun updateThemeSetting(settings: ThemeSettings)
}