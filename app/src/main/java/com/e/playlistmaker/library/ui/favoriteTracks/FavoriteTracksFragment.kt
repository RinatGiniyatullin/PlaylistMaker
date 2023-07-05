package com.e.playlistmaker.library.ui.favoriteTracks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.playlistmaker.App
import com.e.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.e.playlistmaker.player.ui.AudioPlayerActivity
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.ui.SearchFragment
import com.e.playlistmaker.search.ui.SearchViewModel
import com.e.playlistmaker.search.ui.TrackAdapter
import debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteTracksFragment : Fragment() {

    companion object {

        fun newInstance() = FavoriteTracksFragment().apply {
            arguments = Bundle().apply {
            }
        }

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    /* private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel {
         parametersOf()
     }*/

    private val favoriteTracksViewModel by viewModel<FavoriteTracksViewModel>()

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

        favoriteTracksViewModel.favoriteLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                FavoriteState.Empty -> showEmptyResult()
                is FavoriteState.FavoriteTracks -> showFavoriteTracks(state.tracks)
            }

        }

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            openTrack(track.trackId)
        }

        // нажатие на трек
        adapter.itemClickListener =
            { track ->
                onTrackClickDebounce(track)
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

   /* override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/

    private fun initAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun openTrack(trackId: String) {

        val playerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
        playerIntent.putExtra(App.TRACK, trackId)
        startActivity(playerIntent)
    }
}