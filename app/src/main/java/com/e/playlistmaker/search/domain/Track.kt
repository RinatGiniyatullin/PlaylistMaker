package com.e.playlistmaker.search.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean = false,
) : Parcelable {

    fun getCoverArtwork512() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    fun getCoverArtwork60() = artworkUrl100.replaceAfterLast('/', "60x60bb.jpg")

    fun getReleaseDateOnlyYear(): String {
        val answer = releaseDate.split("-")
        return answer[0]
    }
}
