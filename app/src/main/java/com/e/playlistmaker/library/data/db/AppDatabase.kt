package com.e.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.e.playlistmaker.library.data.db.dao.PlaylistDao
import com.e.playlistmaker.library.data.db.dao.TrackDao
import com.e.playlistmaker.library.data.db.dao.TracksForPlaylistsDao
import com.e.playlistmaker.library.data.db.entity.PlaylistEntity
import com.e.playlistmaker.library.data.db.entity.TrackEntity
import com.e.playlistmaker.library.data.db.entity.TrackForPlaylistEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, TrackForPlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun tracksForPlaylistDao(): TracksForPlaylistsDao
}