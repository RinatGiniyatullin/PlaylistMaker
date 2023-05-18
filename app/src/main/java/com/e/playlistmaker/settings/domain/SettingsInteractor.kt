package com.e.playlistmaker.settings.domain

interface SettingsInteractor {

    fun getThemeSettings(): ThemeSettings

    fun updateThemeSetting(settings: ThemeSettings)
}