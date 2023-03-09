package com.e.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)

        val sharedPrefs = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE)
        themeSwitcher.setChecked(sharedPrefs.getBoolean(DARK_THEME_KEY, false))
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit().putBoolean(DARK_THEME_KEY,checked).apply()
        }


        val shareApp = findViewById<TextView>(R.id.share_app)
        shareApp.setOnClickListener {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_developer))
            shareAppIntent.type = "*/*"
            startActivity(shareAppIntent)
        }

        val writeInSupport = findViewById<TextView>(R.id.write_in_support)
        writeInSupport.setOnClickListener {
            val writeInSupportIntent = Intent(Intent.ACTION_SENDTO)
            writeInSupportIntent.data = Uri.parse("mailto:")
            writeInSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            writeInSupportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
            writeInSupportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_message))
            startActivity(writeInSupportIntent)
        }

        val userAgreement = findViewById<TextView>(R.id.user_agreement)
        userAgreement.setOnClickListener {
            val userAgreementIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_offer)))
            startActivity(userAgreementIntent)
        }

        val buttonBack = findViewById<TextView>(R.id.settings_back)
        buttonBack.setOnClickListener {
            finish()
        }
    }
}