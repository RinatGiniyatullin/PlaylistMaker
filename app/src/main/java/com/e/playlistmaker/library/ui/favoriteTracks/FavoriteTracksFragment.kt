package com.e.playlistmaker.library.ui.favoriteTracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.e.playlistmaker.player.ui.AudioPlayerFragment
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.ui.TrackAdapter
import debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    companion object {

        fun newInstance() = FavoriteTracksFragment().apply {
            arguments = Bundle().apply {
            }
        }

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private lateinit var binding: FragmentFavoriteTracksBinding

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private val adapter = TrackAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModel.favoriteLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                FavoriteState.Empty -> showEmptyResult()
                is FavoriteState.FavoriteTracks -> showFavoriteTracks(state.tracks)
            }
        }

        adapter.itemClickListener =
            { track ->
                onTrackClickDebounce(track)
            }

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            openTrack(track.trackId)
        }
    }

    private fun showEmptyResult() {
        binding.recyclerView.visibility = View.GONE
        binding.containerForEmptyResult.visibility = View.VISIBLE
    }

    private fun showFavoriteTracks(tracks: List<Track>) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.containerForEmptyResult.visibility = View.GONE

        adapter.audio.clear()
        adapter.audio.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun initAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun openTrack(trackId: String) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_audioPlayerFragment,
            AudioPlayerFragment.createArgs(trackId)
        )
    }
}