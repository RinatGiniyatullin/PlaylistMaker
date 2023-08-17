package com.e.playlistmaker.sharing.domain

import com.e.playlistmaker.sharing.data.ExternalNavigator

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun shareAppWithMessage(message: String) {
        externalNavigator.shareLink(message)
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return APP_LINK
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(myEmail = MY_EMAIL)
    }

    private fun getTermsLink(): String {
        return TERMS_LINK

    }

    companion object {
        private val MY_EMAIL = "rin63@mail.ru"
        private val APP_LINK = "https://practicum.yandex.ru/android-developer/"
        private val TERMS_LINK = "https://yandex.ru/legal/practicum_offer/"
    }
}