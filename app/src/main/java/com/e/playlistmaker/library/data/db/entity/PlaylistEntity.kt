package com.e.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val title: String,
    val description: String,
    val uriForImage: String,
    val tracksId: String,
    val numberOfTracks: Int,
)