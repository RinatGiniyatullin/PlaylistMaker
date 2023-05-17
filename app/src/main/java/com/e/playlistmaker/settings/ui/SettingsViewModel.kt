package com.e.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.playlistmaker.settings.domain.SettingsInteractor
import com.e.playlistmaker.settings.domain.ThemeSettings
import com.e.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private val _theme = MutableLiveData<Boolean>()
    val theme: LiveData<Boolean> = _theme

    init {
        val saveTheme = settingsInteractor.getThemeSettings().darkTheme
        _theme.postValue(saveTheme)
    }

    private var darkTheme: Boolean = false

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun writeInSupport() {
        sharingInteractor.openSupport()
    }

    fun openUserAgreement() {
        sharingInteractor.openTerms()
    }

    fun switchAction(checked: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(checked))
        _theme.postValue(checked)
        switchTheme(checked)
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
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