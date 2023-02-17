package com.e.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
    private val notFound = "Ничего не нашлось"
    private val noConnection =
        "Проблемы со связью\n\nЗагрузка не удалась. Проверьте подключение к интернету"

    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var updateButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputText: EditText

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

        errorImage = findViewById(R.id.error_image)
        errorText = findViewById(R.id.error_text)
        updateButton = findViewById(R.id.update)
        inputText = findViewById(R.id.editText)
        inputText.setText(input)
        inputText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)

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

        val buttonBack = findViewById<TextView>(R.id.search_back)
        buttonBack.setOnClickListener {
            finish()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                input = s.toString()
            }
        }

        inputText.addTextChangedListener(textWatcher)

        recyclerView = findViewById(R.id.recyclerView)
        adapter.audio = audio
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
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