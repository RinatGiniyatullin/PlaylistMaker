package com.e.playlistmaker.library.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.FragmentPlaylistBinding
import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.library.ui.newPlaylist.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    companion object {

        fun newInstance() = PlaylistFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    private val adapter = PlaylistAdapter()

    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private val newPlaylistViewModel by viewModel<NewPlaylistViewModel>()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylist.setOnClickListener { findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment) }

        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.adapter = adapter

        playlistViewModel.showPlaylist()
        playlistViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                PlaylistState.Empty -> showEmptyResult()
                is PlaylistState.Playlists -> showPlaylists(state.playlists)
            }
        }

        newPlaylistViewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            adapter.playlists.clear()
            adapter.playlists.addAll(playlist)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.containerForEmptyPlaylists.visibility = View.GONE
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    private fun showEmptyResult() {
        binding.recyclerView.visibility = View.GONE
        binding.containerForEmptyPlaylists.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
