package com.e.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.e.playlistmaker.R
import com.e.playlistmaker.sharing.domain.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    private val mailSubject: String = context.getString(R.string.mail_subject)
    private val mailMessage: String = context.getString(R.string.mail_message)
    private val typeIntent: String = context.getString(R.string.type_intent)
    private val warning: String = context.getString(R.string.warning)

    override fun shareLink(shareAppLink: String) {
        val shareAppIntent = Intent(Intent.ACTION_SEND)
        shareAppIntent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        shareAppIntent.type = typeIntent
        shareAppIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        checkIntent(shareAppIntent)
    }

    override fun openLink(termsLink: String) {
        val userAgreementIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(termsLink))
        userAgreementIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        checkIntent(userAgreementIntent)
    }

    override fun openEmail(supportEmailData: EmailData) {
        val writeInSupportIntent = Intent(Intent.ACTION_SENDTO)
        writeInSupportIntent.data = Uri.parse(supportEmailData.mailTo)
        writeInSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.myEmail))
        writeInSupportIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject)
        writeInSupportIntent.putExtra(Intent.EXTRA_TEXT, mailMessage)
        writeInSupportIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        checkIntent(writeInSupportIntent)
    }

    private fun checkIntent(intent: Intent) {
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, warning, Toast.LENGTH_LONG).show()
        }
    }
}