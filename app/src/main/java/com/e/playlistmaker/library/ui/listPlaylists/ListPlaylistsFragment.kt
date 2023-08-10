package com.e.playlistmaker.library.ui.listPlaylists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.FragmentListPlaylistsBinding
import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.library.ui.newPlaylist.NewPlaylistViewModel
import com.e.playlistmaker.library.ui.playlist.PlaylistFragment
import debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListPlaylistsFragment : Fragment() {

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit
    private val adapter = ListPlaylistsAdapter()

    private val listPlaylistsViewModel by viewModel<ListPlaylistsViewModel>()
    private val newPlaylistViewModel by viewModel<NewPlaylistViewModel>()

    private var _binding: FragmentListPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentListPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylist.setOnClickListener { findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment) }

        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.adapter = adapter

        adapter.itemClickListener = { playlist ->
            onPlaylistClickDebounce(playlist)
        }

        onPlaylistClickDebounce = debounce<Playlist>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playlist ->
            openPlaylist(playlist)
        }

        listPlaylistsViewModel.showPlaylist()
        listPlaylistsViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                ListPlaylistsState.Empty -> showEmptyResult()
                is ListPlaylistsState.Playlists -> showPlaylists(state.playlists)
            }
        }

        newPlaylistViewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            adapter.playlists.clear()
            adapter.playlists.addAll(playlist)
            adapter.notifyDataSetChanged()
        }
    }

    private fun openPlaylist(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playlistFragment,
            PlaylistFragment.createArgs(playlist.playlistId)
        )
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

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance() = ListPlaylistsFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}
