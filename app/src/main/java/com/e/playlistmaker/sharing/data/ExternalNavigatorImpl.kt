package com.e.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.e.playlistmaker.R
import com.e.playlistmaker.sharing.domain.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    private val mailSubject: String = context.getString(R.string.mail_subject)
    private val mailMessage: String = context.getString(R.string.mail_message)
    private val typeIntent:String = context.getString(R.string.type_intent)

    override fun shareLink(shareAppLink: String) {
        val shareAppIntent = Intent(Intent.ACTION_SEND)
        shareAppIntent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        shareAppIntent.type = typeIntent
        context.startActivity(shareAppIntent)
    }

    override fun openLink(termsLink: String) {
        val userAgreementIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(termsLink))
        context.startActivity(userAgreementIntent)
    }

    override fun openEmail(supportEmailData: EmailData) {
        val writeInSupportIntent = Intent(Intent.ACTION_SENDTO)
        writeInSupportIntent.data = Uri.parse(supportEmailData.mailTo)
        writeInSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.myEmail))
        writeInSupportIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject)
        writeInSupportIntent.putExtra(Intent.EXTRA_TEXT, mailMessage)
        context.startActivity(writeInSupportIntent)
    }
}