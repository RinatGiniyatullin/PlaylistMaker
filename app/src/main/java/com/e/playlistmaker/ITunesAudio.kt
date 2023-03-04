package com.e.playlistmaker

data class ITunesAudio(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: String
)