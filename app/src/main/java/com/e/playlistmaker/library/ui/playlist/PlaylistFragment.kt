package com.e.playlistmaker.library.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.FragmentPlaylistBinding
import com.e.playlistmaker.library.ui.redactor.PlaylistRedactorFragment
import com.e.playlistmaker.player.ui.AudioPlayerFragment
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.ui.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistFragment : Fragment() {

    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var deleteTrackDialog: MaterialAlertDialogBuilder
    private lateinit var shareDialog: MaterialAlertDialogBuilder
    private lateinit var deletePlaylistDialog: MaterialAlertDialogBuilder

    private lateinit var binding: FragmentPlaylistBinding
    private val adapter = TrackAdapter(isHightQuality = false)
    private lateinit var trackId: String
    private lateinit var image: String
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = requireArguments().getInt(PLAYLIST)
        viewModel.loadPlaylist(playlistId)
        initAdapter()
        viewModel.playlistInfo.observe(viewLifecycleOwner) { info ->
            when (info) {
                is PlaylistInfo.Info -> showPage(
                    info.cover,
                    info.title,
                    info.description,
                    info.time,
                    info.tracksCount,
                    info.tracks,
                    info.visibilityForSuffix
                )
            }
        }

        binding.buttonBack.setOnClickListener {
            back()
        }

        viewModel.isPlaylistDeleted.observe(viewLifecycleOwner) { result ->
            if (result) back()
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

        deleteTrackDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.delete_track_title))
            .setMessage(getString(R.string.delete_track_message))
            .setNeutralButton(getString(R.string.No)) { dialog, which -> }
            .setPositiveButton(getString(R.string.Yes)) { dialog, which ->
                viewModel.deleteTrack(trackId, playlistId)
            }

        adapter.onLongClickListener = { track ->
            trackId = track.trackId
            deleteTrackDialog.show()
            true
        }

        shareDialog = MaterialAlertDialogBuilder(requireActivity())
            .setMessage(getString(R.string.no_tracks))
            .setNeutralButton(getString(R.string.ok)) { dialog, which -> }

        binding.share.setOnClickListener {
            share()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
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

        binding.more.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.bottomSheetShare.setOnClickListener {
            share()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        deletePlaylistDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.delete_playlist))
            .setMessage(getString(R.string.delete_playlist_message))
            .setNeutralButton(getString(R.string.No)) { dialog, which -> }
            .setPositiveButton(getString(R.string.Yes)) { dialog, which ->
                viewModel.deletePlaylist(playlistId)
            }

        binding.bottomSheetDeletePlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            deletePlaylistDialog.show()
        }

        binding.bottomSheetEditInfo.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_redactorPlaylistFragment,
                PlaylistRedactorFragment.createArgs(
                    image,
                    binding.title.text.toString(),
                    binding.description.text.toString(),
                    playlistId
                )
            )
        }

    }

    private fun back() {
        findNavController().navigateUp()
    }

    private fun share() {
        if (adapter.audio.isEmpty()) {
            shareDialog.show()
        } else {
            viewModel.sharePlaylist(
                binding.title.text,
                binding.description.text,
                binding.tracks.text,
                adapter.audio
            )
        }
    }

    private fun openTrack(trackId: String) {
        findNavController().navigate(
            R.id.action_playlistFragment_to_audioPlayerFragment,
            AudioPlayerFragment.createArgs(trackId)
        )
    }

    private fun initAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    private fun dpToPixel(dpResources: Int): Int {
        return requireContext().resources.getDimensionPixelSize(dpResources)
    }

    private fun showPage(
        cover: String,
        title: String,
        description: String,
        time: String,
        tracksCount: String,
        tracks: List<Track>,
        visibilityForEmptyTracks: Boolean,
    ) {
        image = cover
        if (cover.isNotEmpty()) {
            Glide.with(binding.cover)
                .load(cover.toUri())
                .placeholder(R.drawable.cover_placeholder)
                .into(binding.cover)
            Glide.with(binding.bottomSheetCover)
                .load(cover.toUri())
                .placeholder(R.drawable.cover_placeholder)
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_2)))
                .into(binding.bottomSheetCover)
        }
        binding.title.text = title
        binding.description.text = description

        if (time.isEmpty()) {
            binding.time.visibility = View.GONE
            setMargins(binding.tracks, 0, dpToPixel(R.dimen.margin_8), 0, 0)
        } else {
            binding.time.text = time
        }

        binding.tracks.text = tracksCount

        adapter.audio.clear()
        adapter.audio.addAll(tracks)
        adapter.notifyDataSetChanged()

        binding.bottomSheetTitle.text = title
        binding.bottomSheetTracks.text = tracksCount
        binding.suffix.visibility = if (visibilityForEmptyTracks) View.VISIBLE else View.GONE
        binding.tracksBottomSheetContainer.visibility =
            if (visibilityForEmptyTracks) View.VISIBLE else View.GONE
    }

    companion object {
        const val PLAYLIST = "playlist"
        const val CLICK_DEBOUNCE_DELAY = 1000L

        fun createArgs(playlistId: Int): Bundle = bundleOf(PLAYLIST to playlistId)
    }
}