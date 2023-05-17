package com.e.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.e.playlistmaker.creator.Creator
import com.e.playlistmaker.databinding.ActivitySettingsBinding
import com.e.playlistmaker.search.ui.Router
import com.e.playlistmaker.sharing.data.ExternalNavigator
import com.e.playlistmaker.sharing.data.ExternalNavigatorImpl

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        router = Router(this)
        val externalNavigator: ExternalNavigator = ExternalNavigatorImpl(this)

        viewModel =
            ViewModelProvider(
                this,
                Creator.provideSettingsViewModelFactory(externalNavigator, this)
            ).get(
                SettingsViewModel::class.java
            )

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
            router.goBack()
        }
    }
}