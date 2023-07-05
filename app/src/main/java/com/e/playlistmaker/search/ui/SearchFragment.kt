package com.e.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.playlistmaker.App
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.FragmentSearchBinding
import com.e.playlistmaker.player.ui.AudioPlayerActivity
import com.e.playlistmaker.search.domain.Track
import debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()

    private val adapter = TrackAdapter()
    private val adapterForHistory = TrackAdapter()

    private lateinit var binding: FragmentSearchBinding

    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var onHistoryTrackClickDebounce: (Track) -> Unit

    private lateinit var trackSearchDebounce: (String) -> Unit


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initAdapterHistory()

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.Empty -> showEmptyResult()
                is SearchState.Error -> showTracksError()
                is SearchState.History -> showHistory(state.tracks, state.clearText)
                SearchState.Loading -> showLoading()
                is SearchState.Tracks -> showTracks(state.tracks)
            }
        }

        // фокус на строку ввода
        binding.inputText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.searchFocusChanged(hasFocus, binding.inputText.text.toString())
        }

        // нажатие на крестик
        binding.clearButton.setOnClickListener {
            viewModel.clearSearchText()
        }

        // ввод текста
        binding.inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce(s.toString())
                binding.clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                binding.containerForHistory.visibility =
                    if (binding.inputText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                binding.containerForError.visibility =
                    if (binding.inputText.hasFocus() && s?.isEmpty() == true) View.GONE else View.VISIBLE
                binding.recyclerView.visibility =
                    if (binding.inputText.hasFocus() && s?.isEmpty() == true) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            openTrack(track.trackId)
            viewModel.openTrack(track)
        }

        // нажатие на трек
        adapter.itemClickListener =
            { track ->
                onTrackClickDebounce(track)
            }

        onHistoryTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            openTrack(track.trackId)
            viewModel.openHistoryTrack(track)
        }

        // нажатие на историю
        adapterForHistory.itemClickListener =
            { track ->
                onHistoryTrackClickDebounce(track)
            }

        // Очистка истории
        binding.buttonHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        trackSearchDebounce = debounce<String>(
            SEARCH_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            true
        ) { text -> loadTracks(text) }
    }

    private fun openTrack(trackId: String) {

        val playerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
        playerIntent.putExtra(App.TRACK, trackId)
        startActivity(playerIntent)
    }

    private fun showHistory(historyTracks: List<Track>, clearText: Boolean) {

        if (clearText) {
            clearSearchText()
            hideKeyboard()
        }
        binding.containerForHistory.visibility = View.VISIBLE
        binding.containerForError.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.containerForProgressBar.visibility = View.GONE

        adapterForHistory.audio.clear()
        adapterForHistory.audio.addAll(historyTracks)
        adapterForHistory.notifyDataSetChanged()
        if (historyTracks.isEmpty()) {
            binding.containerForHistory.visibility = View.GONE
        }
    }

    private fun clearSearchText() {
        binding.inputText.setText("")
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.inputText.windowToken, 0)
    }

    private fun showTracksError() {
        binding.recyclerView.visibility = View.GONE
        binding.containerForHistory.visibility = View.GONE
        binding.containerForProgressBar.visibility = View.GONE
        binding.containerForError.visibility = View.VISIBLE
        binding.errorImage.visibility = View.VISIBLE
        binding.errorText.visibility = View.VISIBLE
        binding.updateButton.visibility = View.VISIBLE

        binding.errorImage.setImageResource(R.drawable.no_connection)
        binding.errorText.text = NO_CONNECTION
        binding.updateButton.setOnClickListener { loadTracks(binding.inputText.text.toString()) }
    }

    private fun showEmptyResult() {
        binding.recyclerView.visibility = View.GONE
        binding.containerForHistory.visibility = View.GONE
        binding.containerForProgressBar.visibility = View.GONE
        binding.containerForError.visibility = View.VISIBLE
        binding.errorImage.visibility = View.VISIBLE
        binding.errorText.visibility = View.VISIBLE
        binding.updateButton.visibility = View.GONE
        binding.errorImage.setImageResource(R.drawable.not_found)
        binding.errorText.text = NOT_FOUND
    }

    private fun showTracks(tracks: List<Track>) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.containerForProgressBar.visibility = View.GONE
        binding.containerForError.visibility = View.GONE
        binding.containerForHistory.visibility = View.GONE

        adapter.audio.clear()
        adapter.audio.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showLoading() {
        adapter.audio.clear()
        adapter.notifyDataSetChanged()

        binding.recyclerView.visibility = View.GONE
        binding.containerForHistory.visibility = View.GONE
        binding.containerForError.visibility = View.GONE
        binding.containerForProgressBar.visibility = View.VISIBLE
    }

    private fun loadTracks(text: String) {
        viewModel.loadTracks(text)
    }

    private fun initAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun initAdapterHistory() {
        binding.recyclerHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHistory.adapter = adapterForHistory
    }

    private fun searchDebounce(text: String) {
        trackSearchDebounce(text)
    }

    companion object {
        const val NOT_FOUND = "Ничего не нашлось"
        const val NO_CONNECTION =
            "Проблемы со связью\n\nЗагрузка не удалась. Проверьте подключение к интернету"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}