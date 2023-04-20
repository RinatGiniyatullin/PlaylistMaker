package com.e.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayer : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 300L
    }

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var track: ITunesAudio? = null
    private lateinit var playButton: ImageButton
    private lateinit var progressTime: TextView
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val buttonBack = findViewById<TextView>(R.id.library_back)
        val cover = findViewById<ImageView>(R.id.cover)
        val trackName = findViewById<TextView>(R.id.trackName)
        val artistName = findViewById<TextView>(R.id.artistName)
        val addButton = findViewById<ImageButton>(R.id.addButton)
        playButton = findViewById(R.id.playButton)
        progressTime = findViewById(R.id.progressTime)
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

        handler = Handler(Looper.getMainLooper())
        track = intent.getParcelableExtra(TRACK)!!

        Glide.with(cover)
            .load(track?.getCoverArtwork())
            .placeholder(R.drawable.cover_placeholder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_small)))
            .into(cover)

        trackName.text = track?.trackName
        artistName.text = track?.artistName

        if (track?.trackTimeMillis != 0L) {
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTimeMillis)
        } else {
            trackTimeGroup.visibility = View.GONE
        }

        if (track?.collectionName!!.isNotEmpty()) {
            collectionName.text = track?.collectionName
        } else {
            collectionNameGroup.visibility = View.GONE
        }

        if (track?.releaseDate!!.isNotEmpty()) {
            releaseDate.text = track?.getReleaseDateOnlyYear()
        } else {
            releaseDateGroup.visibility = View.GONE
        }

        if (track?.primaryGenreName!!.isNotEmpty()) {
            genreName.text = track?.primaryGenreName
        } else {
            genreNameGroup.visibility = View.GONE
        }

        if (track?.country!!.isNotEmpty()) {
            country.text = track?.country
        } else {
            countryGroup.visibility = View.GONE
        }

        buttonBack.setOnClickListener {
            finish()
        }

        preparePlayer()
        playButton.setOnClickListener {
            playbackControl()
        }
    }

    private val run = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING)
                progressTime.text =
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
            handler?.postDelayed(this, DELAY)
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler?.removeCallbacks(run)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED
            handler?.removeCallbacks(run)
            progressTime.text = "00:00"
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        handler?.post(run)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
        handler?.removeCallbacks(run)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }
}