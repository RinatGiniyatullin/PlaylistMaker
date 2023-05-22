package com.e.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.theme.observe(this) { checked ->
            binding.themeSwitcher.isChecked = checked
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchAction(checked)
        }

        binding.shareApp.setOnClickListener {
            viewModel.shareApp()
        }

        binding.writeInSupport.setOnClickListener {
            viewModel.writeInSupport()
        }

       binding.userAgreement.setOnClickListener {
            viewModel.openUserAgreement()
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
}