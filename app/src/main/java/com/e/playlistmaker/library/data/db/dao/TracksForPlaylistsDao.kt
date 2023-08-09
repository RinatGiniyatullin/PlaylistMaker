package com.e.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.e.playlistmaker.library.data.db.entity.TrackForPlaylistEntity

@Dao
interface TracksForPlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInPlaylist(track: TrackForPlaylistEntity)

    @Query("SELECT * FROM tracks_for_playlists_table")
    suspend fun getTracksForPlaylists(): List<TrackForPlaylistEntity>

    @Query("DELETE FROM tracks_for_playlists_table WHERE trackId = :id")
    suspend fun deleteTrack(id: String)

}