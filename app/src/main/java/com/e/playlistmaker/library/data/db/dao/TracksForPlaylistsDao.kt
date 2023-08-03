package com.e.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.e.playlistmaker.library.data.db.entity.TrackForPlaylistEntity

@Dao
interface TracksForPlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInPlaylist(track: TrackForPlaylistEntity)
}