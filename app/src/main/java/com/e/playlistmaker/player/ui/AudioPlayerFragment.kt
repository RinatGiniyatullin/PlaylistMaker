package com.e.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.library.ui.newPlaylist.NewPlaylistViewModel
import com.e.playlistmaker.library.ui.listPlaylists.ListPlaylistsState
import com.e.playlistmaker.library.ui.listPlaylists.ListPlaylistsViewModel
import com.e.playlistmaker.player.ui.DateTimeFormatter.PROGRESS_FORMAT
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.ui.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment : Fragment() {

    private lateinit var binding: FragmentAudioPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()
    private val searchViewModel by viewModel<SearchViewModel>()
    private val newPlaylistViewModel by viewModel<NewPlaylistViewModel>()
    private val listPlaylistsViewModel by viewModel<ListPlaylistsViewModel>()

    private val adapter = PlayerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAudioPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackId = requireArguments().getString(TRACK_ID)

        val bottomSheetContainer = binding.bottomSheet

        initAdapter()

        newPlaylistViewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            adapter.playlists.clear()
            adapter.playlists.addAll(playlist)
            adapter.notifyDataSetChanged()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            listPlaylistsViewModel.showPlaylist()
            listPlaylistsViewModel.state.observe(viewLifecycleOwner) { state ->
                when (state) {
                    ListPlaylistsState.Empty -> showEmptyResult()
                    is ListPlaylistsState.Playlists -> showPlaylists(state.playlists)
                }
            }
        }

        binding.newPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }

        binding.recyclerView.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        adapter.itemClickListener = { playlist ->
            viewModel.addTrackToPlaylist(trackId!!, playlist)
        }

        viewModel.addTrackLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AddResultState.NegativeResult -> {
                    Toast.makeText(
                        requireActivity(),
                        "Трек уже добавлен в плейлист ${result.playlist.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is AddResultState.PositiveResult -> {
                    Toast.makeText(
                        requireActivity(),
                        "Добавлено в плейлист ${result.playlist.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }

        viewModel.showTrackLiveData.observe(viewLifecycleOwner) { track ->
            showTrackCover(track.getCoverArtwork512())
            showTrackName(track.trackName)
            showArtistName(track.artistName)

            if (track.trackTimeMillis != 0L) showTrackTimeText(
                SimpleDateFormat(
                    PROGRESS_FORMAT,
                    Locale.getDefault()
                ).format(track.trackTimeMillis)
            )
            if (track.collectionName.isNotEmpty()) showCollectionNameText(track.collectionName)
            if (track.getReleaseDateOnlyYear()
                    .isNotEmpty()
            ) showReleaseDateText(track.getReleaseDateOnlyYear())
            if (track.primaryGenreName.isNotEmpty()) showGenreNameText(track.primaryGenreName)
            if (track.country.isNotEmpty()) showCountryText(track.country)
            showLikeButtonState(track)
        }

        viewModel.timeLiveData.observe(viewLifecycleOwner) { currentTime ->
            updateProgressTime(currentTime)
        }

        viewModel.playButtonStateLiveData.observe(viewLifecycleOwner) { state ->
            setImagePlayButton(state.imageButton)
        }

        viewModel.likeButtonStateLiveData.observe(viewLifecycleOwner) { state ->
            setImageLikeButton(state.imageButton)
        }

        searchViewModel.likeState.observe(viewLifecycleOwner) { state ->
            setImageLikeButton(state.imageButton)
        }

        viewModel.loadTrack(trackId!!)

        binding.playButton.setOnClickListener {
            viewModel.playTrack(trackId)
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked(trackId)
        }
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        binding.recyclerView.visibility = View.VISIBLE
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    private fun showEmptyResult() {
        binding.recyclerView.visibility = View.GONE
    }

    private fun initAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = adapter
    }

    private fun showLikeButtonState(track: Track) {
        if (track.isFavorite) {
            binding.likeButton.setImageResource(R.drawable.like_ok)
        } else {
            binding.likeButton.setImageResource(R.drawable.like)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    private fun showTrackCover(image: String) {

        Glide.with(binding.cover)
            .load(image)
            .placeholder(R.drawable.cover_placeholder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_8)))
            .into(binding.cover)
    }

    private fun showTrackName(trackNameText: String) {
        binding.trackName.text = trackNameText
    }

    private fun showArtistName(artistNameText: String) {
        binding.artistName.text = artistNameText
    }

    private fun showTrackTimeText(trackTimeText: String) {
        binding.trackTimeGroup.visibility = View.VISIBLE
        binding.trackTimeValue.text = trackTimeText
    }

    private fun showCollectionNameText(collectionNameText: String) {
        binding.collectionNameGroup.visibility = View.VISIBLE
        binding.collectionNameValue.text = collectionNameText
    }

    private fun showReleaseDateText(releaseDateText: String) {
        binding.releaseDateGroup.visibility = View.VISIBLE
        binding.releaseDateValue.text = releaseDateText
    }

    private fun showGenreNameText(genreNameText: String) {
        binding.genreNameGroup.visibility = View.VISIBLE
        binding.genreNameValue.text = genreNameText
    }

    private fun showCountryText(countryText: String) {
        binding.countryGroup.visibility = View.VISIBLE
        binding.countryValue.text = countryText
    }

    private fun setImagePlayButton(image: Int) {
        binding.playButton.setImageResource(image)
    }

    private fun setImageLikeButton(imageButton: Int) {
        binding.likeButton.setImageResource(imageButton)
    }

    private fun updateProgressTime(currentTime: String) {
        binding.progressTime.text = currentTime
    }

    companion object {
        const val TRACK_ID = "trackId"

        fun createArgs(trackId: String): Bundle = bundleOf(TRACK_ID to trackId)
    }
}