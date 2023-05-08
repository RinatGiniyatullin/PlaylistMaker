package com.e.playlistmaker.search.presentation

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.playlistmaker.HISTORY_PREFERENCES
import com.e.playlistmaker.search.data.ITunesApi
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.R
import com.e.playlistmaker.search.data.HistorySearchDataStore
import com.e.playlistmaker.search.data.SearchRepository
import com.e.playlistmaker.search.domain.SearchInteractor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), SearchScreenView {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val adapter = TrackAdapter()
    private val adapterForHistory = TrackAdapter()
    private lateinit var historySearchDataStore: HistorySearchDataStore
    private lateinit var sharedPrefHistory: SharedPreferences
    private lateinit var inputText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var buttonBack: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var updateButton: Button
    private lateinit var containerForHistory: ViewGroup
    private lateinit var containerForError: ViewGroup
    private lateinit var containerForProgressBar: ViewGroup
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var buttonHistory: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initHistory()
        initView()
        initAdapter()
        initAdapterHistory()
        val iTunesBaseUrl = "https://itunes.apple.com"

        val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val repository = SearchRepository(retrofit.create(ITunesApi::class.java))

        presenter =
            SearchPresenter(
                view = this,
                interactor = SearchInteractor(historySearchDataStore, repository),
                router = Router(this)
            )

        // фокус на строку ввода
        inputText.setOnFocusChangeListener { _, hasFocus ->
            presenter.searchFocusChanged(hasFocus, inputText.text.toString())
        }

        // нажатие на крестик
        clearButton.setOnClickListener {
            presenter.searchTextClearClicked()
        }

        // нажатие на кнопку назад
        buttonBack.setOnClickListener {
            presenter.backButtonClicked()
        }

        // ввод текста
        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                containerForHistory.visibility =
                    if (inputText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                containerForError.visibility =
                    if (inputText.hasFocus() && s?.isEmpty() == true) View.GONE else View.VISIBLE
                recyclerView.visibility =
                    if (inputText.hasFocus() && s?.isEmpty() == true) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // нажатие на трек
        adapter.itemClickListener =
            { track ->
                if (clickDebounce()) {
                    presenter.onTrackClicked(track)
                }
            }

        // нажатие на историю
        adapterForHistory.itemClickListener =
            { track ->
                if (clickDebounce()) {
                    presenter.onHistoryTrackClicked(track)
                }
            }

        // Очистка истории
        buttonHistory.setOnClickListener {
            presenter.onHistoryClearClicked()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyView()
    }
    override fun showHistory(historyTracks: List<Track>) {
        containerForHistory.visibility = View.VISIBLE
        containerForError.visibility = View.GONE
        recyclerView.visibility = View.GONE
        containerForProgressBar.visibility = View.GONE

        adapterForHistory.audio.clear()
        adapterForHistory.audio.addAll(historyTracks)
        adapterForHistory.notifyDataSetChanged()
        if (historyTracks.isEmpty()) {
            containerForHistory.visibility = View.GONE
        }
    }

    override fun clearSearchText() {
        inputText.setText("")
    }

    override fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(inputText.windowToken, 0)
    }

    override fun showEmptyResult() {
        recyclerView.visibility = View.GONE
        containerForHistory.visibility = View.GONE
        containerForProgressBar.visibility = View.GONE
        containerForError.visibility = View.VISIBLE
        errorImage.visibility = View.VISIBLE
        errorText.visibility = View.VISIBLE
        updateButton.visibility = View.GONE
        errorImage.setImageResource(R.drawable.not_found)
        errorText.text = NOT_FOUND
    }

    override fun showTracks(tracks: List<Track>) {
        recyclerView.visibility = View.VISIBLE
        containerForProgressBar.visibility = View.GONE
        containerForError.visibility = View.GONE
        containerForHistory.visibility = View.GONE

        adapter.audio.clear()
        adapter.audio.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    override fun showTracksError() {
        recyclerView.visibility = View.GONE
        containerForHistory.visibility = View.GONE
        containerForProgressBar.visibility = View.GONE
        containerForError.visibility = View.VISIBLE
        errorImage.visibility = View.VISIBLE
        errorText.visibility = View.VISIBLE
        updateButton.visibility = View.VISIBLE

        errorImage.setImageResource(R.drawable.no_connection)
        errorText.text = NO_CONNECTION
        updateButton.setOnClickListener { loadTracks() }
    }

    override fun showLoading() {
        adapter.audio.clear()
        adapter.notifyDataSetChanged()

        recyclerView.visibility = View.GONE
        containerForHistory.visibility = View.GONE
        containerForError.visibility = View.GONE
        containerForProgressBar.visibility = View.VISIBLE
    }

    override fun updateHistoryTracks(historyTracks: MutableList<Track>) {
        adapterForHistory.audio.clear()
        adapterForHistory.audio.addAll(historyTracks)
        adapterForHistory.notifyDataSetChanged()
    }

    private fun loadTracks() {
        presenter.loadTracks(inputText.text.toString())
    }

    private fun initHistory() {
        sharedPrefHistory = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE)
        historySearchDataStore = HistorySearchDataStore(sharedPrefHistory)
    }

    private fun initView() {

        inputText = findViewById(R.id.editText)
        clearButton = findViewById(R.id.clearIcon)
        buttonBack = findViewById(R.id.search_back)
        recyclerView = findViewById(R.id.recyclerView)
        errorImage = findViewById(R.id.error_image)
        errorText = findViewById(R.id.error_text)
        updateButton = findViewById(R.id.update)
        containerForHistory = findViewById(R.id.containerForHistory)
        containerForError = findViewById(R.id.containerForError)
        containerForProgressBar = findViewById(R.id.containerForProgressBar)
        recyclerHistory = findViewById(R.id.recyclerHistory)
        buttonHistory = findViewById(R.id.buttonHistory)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun initAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun initAdapterHistory() {
        recyclerHistory.layoutManager = LinearLayoutManager(this)
        recyclerHistory.adapter = adapterForHistory
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        val searchRunnable = Runnable { loadTracks() }
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        const val NOT_FOUND = "Ничего не нашлось"
        const val NO_CONNECTION =
            "Проблемы со связью\n\nЗагрузка не удалась. Проверьте подключение к интернету"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}