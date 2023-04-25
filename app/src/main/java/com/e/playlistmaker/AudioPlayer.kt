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

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var track: ITunesAudio
    private lateinit var buttonBack: TextView
    private lateinit var cover: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var addButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var progressTime: TextView
    private lateinit var likeButton: ImageButton
    private lateinit var trackTime: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var genreName: TextView
    private lateinit var country: TextView
    private lateinit var trackTimeGroup: androidx.constraintlayout.widget.Group
    private lateinit var collectionNameGroup: androidx.constraintlayout.widget.Group
    private lateinit var releaseDateGroup: androidx.constraintlayout.widget.Group
    private lateinit var genreNameGroup: androidx.constraintlayout.widget.Group
    private lateinit var countryGroup: androidx.constraintlayout.widget.Group
    private lateinit var handler: Handler

    private val run = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING)
                progressTime.text =
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
            handler.postDelayed(this, DELAY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        viewInitialization()

        handler = Handler(Looper.getMainLooper())
        track = intent.getParcelableExtra(TRACK)!!

        Glide.with(cover)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.cover_placeholder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_small)))
            .into(cover)

        trackName.text = track.trackName
        artistName.text = track.artistName

        visiblyTrackTimeGroup()
        visiblyCollectionNameGroup()
        visiblyReleaseDateGroup()
        visiblyGenreNameGroup()
        visiblyCountryGroup()

        buttonBack.setOnClickListener {
            finish()
        }

        preparePlayer()

        playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(run)
    }

    private fun viewInitialization() {
        buttonBack = findViewById(R.id.library_back)
        cover = findViewById(R.id.cover)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        addButton = findViewById(R.id.addButton)
        playButton = findViewById(R.id.playButton)
        progressTime = findViewById(R.id.progressTime)
        likeButton = findViewById(R.id.likeButton)
        trackTime = findViewById(R.id.trackTimeValue)
        collectionName = findViewById(R.id.collectionNameValue)
        releaseDate = findViewById(R.id.releaseDateValue)
        genreName = findViewById(R.id.genreNameValue)
        country = findViewById(R.id.countryValue)
        trackTimeGroup = findViewById(R.id.trackTimeGroup)
        collectionNameGroup = findViewById(R.id.collectionNameGroup)
        releaseDateGroup = findViewById(R.id.releaseDateGroup)
        genreNameGroup = findViewById(R.id.genreNameGroup)
        countryGroup = findViewById(R.id.countryGroup)
    }

    private fun visiblyTrackTimeGroup() {
        if (track.trackTimeMillis != 0L) {
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        } else {
            trackTimeGroup.visibility = View.GONE
        }
    }

    private fun visiblyCollectionNameGroup() {
        if (track.collectionName.isNotEmpty()) {
            collectionName.text = track.collectionName
        } else {
            collectionNameGroup.visibility = View.GONE
        }
    }

    private fun visiblyReleaseDateGroup() {
        if (track.releaseDate.isNotEmpty()) {
            releaseDate.text = track.getReleaseDateOnlyYear()
        } else {
            releaseDateGroup.visibility = View.GONE
        }
    }

    private fun visiblyGenreNameGroup() {
        if (track.primaryGenreName.isNotEmpty()) {
            genreName.text = track.primaryGenreName
        } else {
            genreNameGroup.visibility = View.GONE
        }
    }

    private fun visiblyCountryGroup() {
        if (track.country.isNotEmpty()) {
            country.text = track.country
        } else {
            countryGroup.visibility = View.GONE
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED
            handler.removeCallbacks(run)
            progressTime.text = DEFAULT_MM_SS
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        handler.post(run)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
        handler.removeCallbacks(run)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 300L
        private const val DEFAULT_MM_SS = "00:00"
    }
}