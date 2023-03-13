package com.e.playlistmaker

data class ITunesAudio(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun getReleaseDateOnlyYear(): String {
        val answer = releaseDate.split("-")
        return answer[0]
    }

}