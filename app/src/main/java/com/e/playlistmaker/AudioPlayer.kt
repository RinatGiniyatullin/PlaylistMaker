package com.e.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val buttonBack = findViewById<TextView>(R.id.library_back)
        val cover = findViewById<ImageView>(R.id.cover)
        val trackName = findViewById<TextView>(R.id.trackName)
        val artistName = findViewById<TextView>(R.id.artistName)
        val addButton = findViewById<ImageButton>(R.id.addButton)
        val playButton = findViewById<ImageButton>(R.id.playButton)
        val likeButton = findViewById<ImageButton>(R.id.likeButton)
        val trackTime = findViewById<TextView>(R.id.trackTimeValue)
        val collectionName = findViewById<TextView>(R.id.collectionNameValue)
        val releaseDate = findViewById<TextView>(R.id.releaseDateValue)
        val genreName = findViewById<TextView>(R.id.genreNameValue)
        val country = findViewById<TextView>(R.id.countryValue)
        val trackTimeGroup =
            findViewById<androidx.constraintlayout.widget.Group>(R.id.trackTimeGroup)
        val collectionNameGroup =
            findViewById<androidx.constraintlayout.widget.Group>(R.id.collectionNameGroup)
        val releaseDateGroup =
            findViewById<androidx.constraintlayout.widget.Group>(R.id.releaseDateGroup)
        val genreNameGroup =
            findViewById<androidx.constraintlayout.widget.Group>(R.id.genreNameGroup)
        val countryGroup = findViewById<androidx.constraintlayout.widget.Group>(R.id.countryGroup)


        val json = intent.getStringExtra(TRACK)
        val track = Gson().fromJson(json, ITunesAudio::class.java)

        Glide.with(cover)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.cover_placeholder)
            .transform(RoundedCorners(8))
            .into(cover)

        trackName.text = track.trackName
        artistName.text = track.artistName

        if (track.trackTimeMillis != 0L) {
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        } else {
            trackTimeGroup.visibility = View.GONE
        }

        if (track.collectionName.isNotEmpty()) {
            collectionName.text = track.collectionName
        } else {
            collectionNameGroup.visibility = View.GONE
        }

        if (track.releaseDate.isNotEmpty()) {
            releaseDate.text = track.getReleaseDateOnlyYear()
        } else {
            releaseDateGroup.visibility = View.GONE
        }

        if (track.primaryGenreName.isNotEmpty()) {
            genreName.text = track.primaryGenreName
        } else {
            genreNameGroup.visibility = View.GONE
        }

        if (track.country.isNotEmpty()) {
            country.text = track.country
        } else {
            countryGroup.visibility = View.GONE
        }

        buttonBack.setOnClickListener {
            finish()
        }


    }
}