package com.e.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.FragmentLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentLibraryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlist)
            }

        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}