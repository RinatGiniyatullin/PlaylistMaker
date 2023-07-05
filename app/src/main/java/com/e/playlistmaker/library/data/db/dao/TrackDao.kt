package com.e.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.e.playlistmaker.library.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks_table")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT * FROM tracks_table")
    suspend fun getIdFavoriteTracks(): List<String>
}