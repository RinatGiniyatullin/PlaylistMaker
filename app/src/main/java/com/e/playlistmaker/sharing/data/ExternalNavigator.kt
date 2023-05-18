package com.e.playlistmaker.sharing.data

import com.e.playlistmaker.sharing.domain.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)

    fun openLink(termsLink: String)

    fun openEmail(supportEmailData: EmailData)
}