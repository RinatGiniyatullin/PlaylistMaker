package com.e.playlistmaker.library.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator


class LibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlist)
            }

        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}