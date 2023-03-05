package com.e.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val TEXT = "TEXT"
    }

    var input: String? = null
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val audio = ArrayList<ITunesAudio>()
    private val adapter = TrackAdapter()
    private var audioHistory = ArrayList<ITunesAudio>()
    private val adapterForHistory = TrackAdapter()
    private val notFound = "Ничего не нашлось"
    private val noConnection =
        "Проблемы со связью\n\nЗагрузка не удалась. Проверьте подключение к интернету"
    private lateinit var historySearch: HistorySearch
    private lateinit var sharedPrefHistory: SharedPreferences
    private lateinit var inputText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var updateButton: Button
    private lateinit var containerForHistory: ViewGroup
    private lateinit var containerForError: ViewGroup
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var buttonHistory: Button

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT, input)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        input = savedInstanceState.getString(TEXT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPrefHistory = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE)
        inputText = findViewById(R.id.editText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val buttonBack = findViewById<TextView>(R.id.search_back)
        recyclerView = findViewById(R.id.recyclerView)
        errorImage = findViewById(R.id.error_image)
        errorText = findViewById(R.id.error_text)
        updateButton = findViewById(R.id.update)
        containerForHistory = findViewById(R.id.containerForHistory)
        containerForError = findViewById(R.id.containerForError)
        recyclerHistory = findViewById(R.id.recyclerHistory)
        buttonHistory = findViewById(R.id.buttonHistory)
        historySearch = HistorySearch(sharedPrefHistory)

        inputText.setText(input)
        inputText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        // фокус на строку ввода
        inputText.setOnFocusChangeListener { view, hasFocus ->
            containerForHistory.visibility =
                if (hasFocus && inputText.text.isEmpty() && historySearch.readFromJson()
                        .isNotEmpty()
                ) View.VISIBLE else View.GONE
            containerForError.visibility =
                if (hasFocus && inputText.text.isEmpty()) View.GONE else View.VISIBLE
            recyclerView.visibility =
                if (hasFocus && inputText.text.isEmpty()) View.GONE else View.VISIBLE
        }

        // нажатие на крестик
        clearButton.setOnClickListener {
            inputText.setText("")
            audio.clear()
            adapter.notifyDataSetChanged()
            errorImage.visibility = View.GONE
            errorText.visibility = View.GONE
            updateButton.visibility = View.GONE
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputText.windowToken, 0)
        }

        // нажатие на кнопку назад
        buttonBack.setOnClickListener {
            finish()
        }

        // ввод текста
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)

                containerForHistory.visibility =
                    if (inputText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                containerForError.visibility =
                    if (inputText.hasFocus() && s?.isEmpty() == true) View.GONE else View.VISIBLE
                recyclerView.visibility =
                    if (inputText.hasFocus() && s?.isEmpty() == true) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                input = s.toString()
            }
        }

        inputText.addTextChangedListener(textWatcher)

        // список треков
        adapter.audio = audio
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // список истории
        audioHistory.addAll(historySearch.readFromJson())
        adapterForHistory.audio = audioHistory
        recyclerHistory.layoutManager = LinearLayoutManager(this)
        recyclerHistory.adapter = adapterForHistory
        adapterForHistory.notifyDataSetChanged()

        // нажатие на трек
        adapter.itemClickListener = { track ->
            if (audioHistory.contains(track)) {
                audioHistory.remove(track)
                audioHistory.add(0, track)
            } else {
                audioHistory.add(0, track)
            }
            if (audioHistory.size == 10) {
                audioHistory.removeAt(9)
            }
            historySearch.writeToJson(audioHistory)
            adapterForHistory.notifyDataSetChanged()
        }


        // Очистка истории
        buttonHistory.setOnClickListener {
            historySearch.clearHistory()
            audioHistory.clear()
            containerForHistory.visibility = View.GONE
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showMessage(text: String) {
        when (text) {
            notFound -> {
                errorImage.visibility = View.VISIBLE
                errorText.visibility = View.VISIBLE
                updateButton.visibility = View.GONE
                audio.clear()
                adapter.notifyDataSetChanged()
                recyclerView.visibility = View.GONE
                errorImage.setImageResource(R.drawable.not_found)
                errorText.setText(text)

            }
            noConnection -> {
                errorImage.visibility = View.VISIBLE
                errorText.visibility = View.VISIBLE
                updateButton.visibility = View.VISIBLE
                audio.clear()
                adapter.notifyDataSetChanged()
                recyclerView.visibility = View.GONE
                errorImage.setImageResource(R.drawable.no_connection)
                errorText.setText(text)
                updateButton.setOnClickListener { search() }
            }
            else -> {
                errorImage.visibility = View.GONE
                errorText.visibility = View.GONE
                updateButton.visibility = View.GONE
            }
        }
    }

    private fun search() {
        if (inputText.text.isNotEmpty()) {
            iTunesService.search(inputText.text.toString())
                .enqueue(object : Callback<ITunesResponse> {
                    override fun onResponse(
                        call: Call<ITunesResponse>,
                        response: Response<ITunesResponse>
                    ) {
                        if (response.code() == 200) {
                            recyclerView.visibility = View.VISIBLE
                            audio.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                audio.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            } else {
                                showMessage(notFound)
                            }
                        } else {
                            showMessage(noConnection)
                        }
                    }

                    override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                        showMessage(noConnection)
                    }
                })
        }
    }
}