package com.e.playlistmaker.player.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.App.Companion.TRACK
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.e.playlistmaker.player.ui.DateTimeFormatter.PROGRESS_FORMAT
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackId = intent.getStringExtra(TRACK).orEmpty()

        viewModel.showTrackLiveData.observe(this) { track ->
            showTrackCover(track.getCoverArtwork())
            showTrackName(track.trackName)
            showArtistName(track.artistName)

            if (track.trackTimeMillis != 0L) showTrackTimeText(
                SimpleDateFormat(
                    PROGRESS_FORMAT,
                    Locale.getDefault()
                ).format(track.trackTimeMillis)
            )
            if (track.collectionName.isNotEmpty()) showCollectionNameText(track.collectionName)
            if (track.getReleaseDateOnlyYear()
                    .isNotEmpty()
            ) showReleaseDateText(track.getReleaseDateOnlyYear())
            if (track.primaryGenreName.isNotEmpty()) showGenreNameText(track.primaryGenreName)
            if (track.country.isNotEmpty()) showCountryText(track.country)
        }

        viewModel.timeLiveData.observe(this) { currentTime ->
            updateProgressTime(currentTime)
        }

        viewModel.buttonStateLiveData.observe(this) { state ->
            setImageButton(state.imageButton)
        }

        viewModel.loadTrack(trackId)

        binding.playButton.setOnClickListener {
            viewModel.playTrack(trackId)
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    private fun showTrackCover(image: String) {
        Glide.with(binding.cover)
            .load(image)
            .placeholder(R.drawable.cover_placeholder)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_small)))
            .into(binding.cover)
    }

    private fun showTrackName(trackNameText: String) {
        binding.trackName.text = trackNameText
    }

    private fun showArtistName(artistNameText: String) {
        binding.artistName.text = artistNameText
    }

    private fun showTrackTimeText(trackTimeText: String) {
        binding.trackTimeGroup.visibility = View.VISIBLE
        binding.trackTimeValue.text = trackTimeText
    }

    private fun showCollectionNameText(collectionNameText: String) {
        binding.collectionNameGroup.visibility = View.VISIBLE
        binding.collectionNameValue.text = collectionNameText
    }

    private fun showReleaseDateText(releaseDateText: String) {
        binding.releaseDateGroup.visibility = View.VISIBLE
        binding.releaseDateValue.text = releaseDateText
    }

    private fun showGenreNameText(genreNameText: String) {
        binding.genreNameGroup.visibility = View.VISIBLE
        binding.genreNameValue.text = genreNameText
    }

    private fun showCountryText(countryText: String) {
        binding.countryGroup.visibility = View.VISIBLE
        binding.countryValue.text = countryText
    }

    private fun setImageButton(image: Int) {
        binding.playButton.setImageResource(image)
    }

    private fun updateProgressTime(currentTime: String) {
        binding.progressTime.text = currentTime
    }
}