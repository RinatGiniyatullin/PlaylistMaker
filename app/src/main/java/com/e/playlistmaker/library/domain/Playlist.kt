package com.e.playlistmaker.library.domain


data class Playlist(
    val playlistId: Int,
    val title: String,
    val description: String = "",
    val uriForImage: String = "",
    val tracksId: MutableList<String> = mutableListOf(),
    var numberOfTracks: Int = 0,
)