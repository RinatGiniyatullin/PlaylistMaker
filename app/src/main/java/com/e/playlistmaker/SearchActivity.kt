package com.e.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class SearchActivity : AppCompatActivity() {
    companion object {
        const val TEXT = "TEXT"
    }

    var input: String? = null

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

        val inputText = findViewById<EditText>(R.id.editText)
        inputText.setText(input)

        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        clearButton.setOnClickListener {
            inputText.setText("")
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
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}