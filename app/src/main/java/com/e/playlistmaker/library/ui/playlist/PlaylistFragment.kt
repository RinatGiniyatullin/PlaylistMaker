package com.e.playlistmaker.library.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.e.playlistmaker.databinding.FragmentPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    companion object {

        fun newInstance() = PlaylistFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    private val playlistViewModel: PlaylistViewModel by viewModel {
        parametersOf()
    }

    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistViewModel.liveData.observe(viewLifecycleOwner) {

        }
    }
}
