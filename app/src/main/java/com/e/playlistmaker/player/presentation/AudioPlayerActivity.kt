package com.e.playlistmaker.player.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.HISTORY_PREFERENCES
import com.e.playlistmaker.R
import com.e.playlistmaker.TRACK
import com.e.playlistmaker.player.data.Player
import com.e.playlistmaker.player.domain.PlayerInteractor
import com.e.playlistmaker.search.data.HistorySearchDataStore
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity(), PlayerScreenView {

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
    private lateinit var presenter: AudioPlayerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        viewInitialization()
        val sharedPrefHistory = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE)
        val historySearchDataStore = HistorySearchDataStore(sharedPrefHistory)
        val trackId = intent.getStringExtra(TRACK)!!
        val player = Player(trackId, sharedPrefHistory)

        presenter = AudioPlayerPresenter(
            view = this,
            interactor = PlayerInteractor(historySearchDataStore, player)
        )

        presenter.loadTrack(trackId)

        playButton.setOnClickListener {
            presenter.playButtonClicked()
        }

        buttonBack.setOnClickListener {
            presenter.backButtonClicked()
        }
    }

    override fun goBack() {
        finish()
    }

    override fun onPause() {
        super.onPause()
        presenter.playerPaused()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.playerOnDestroyed()
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

    override fun showTrackCover(image: String) {
        Glide.with(cover)
            .load(image)
            .placeholder(R.drawable.cover_placeholder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_small)))
            .into(cover)
    }

    override fun showTrackName(trackNameText: String) {
        trackName.text = trackNameText
    }

    override fun showArtistName(artistNameText: String) {
        artistName.text = artistNameText
    }

    override fun showTrackTimeText(trackTimeText: String) {
        trackTime.text = trackTimeText
    }

    override fun goneTrackTimeGroup() {
        trackTimeGroup.visibility = View.GONE
    }

    override fun showCollectionNameText(collectionNameText: String) {
        collectionName.text = collectionNameText
    }

    override fun goneCollectionNameGroup() {
        collectionNameGroup.visibility = View.GONE
    }

    override fun showReleaseDateText(releaseDateText: String) {
        releaseDate.text = releaseDateText
    }

    override fun goneReleaseDateGroup() {
        releaseDateGroup.visibility = View.GONE
    }

    override fun showGenreNameText(genreNameText: String) {
        genreName.text = genreNameText
    }

    override fun goneGenreNameGroup() {
        genreNameGroup.visibility = View.GONE
    }

    override fun showCountryText(countryText: String) {
        country.text = countryText
    }

    override fun goneCountryTextGroup() {
        countryGroup.visibility = View.GONE
    }

    override fun setEnableButton(b: Boolean) {
        playButton.isEnabled = b
    }

    override fun setImageButton(image: Int) {
        playButton.setImageResource(image)
    }

    override fun updadeProgressTime(currentTime: Int) {
        progressTime.text =
            SimpleDateFormat(PROGRESS_FORMAT, Locale.getDefault()).format(currentTime)
    }

    companion object {
        private const val PROGRESS_FORMAT = "mm:ss"
    }
}