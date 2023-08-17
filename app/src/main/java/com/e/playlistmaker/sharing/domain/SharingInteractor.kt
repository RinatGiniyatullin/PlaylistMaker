package com.e.playlistmaker.sharing.domain

interface SharingInteractor {

    fun shareApp()
    fun shareAppWithMessage(message: String)

    fun openTerms()

    fun openSupport()
}