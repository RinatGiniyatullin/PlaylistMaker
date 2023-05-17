package com.e.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.e.playlistmaker.settings.domain.SettingsInteractor
import com.e.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModelFactory(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(sharingInteractor, settingsInteractor) as T
    }
}