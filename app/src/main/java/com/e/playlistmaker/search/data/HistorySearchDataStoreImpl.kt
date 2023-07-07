package com.e.playlistmaker.search.data

import android.content.SharedPreferences
import com.e.playlistmaker.library.data.db.AppDatabase
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.domain.HistorySearchDataStore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HistorySearchDataStoreImpl(
    private val sharedPreferences: SharedPreferences,
    private val database: AppDatabase,
) :
    HistorySearchDataStore {
    private val TRACK_KEY = "Track_key"

    // Очистка истории
    override fun clearHistory() {
        sharedPreferences.edit().remove(TRACK_KEY).apply()
    }

    // чтение
    override fun getHistory(): Flow<List<Track>> = flow {
        val json = sharedPreferences.getString(TRACK_KEY, null)
        val historyTracks = Gson().fromJson(json, Array<Track>::class.java).toList()
        val idFavoriteTracks = database.trackDao().getIdFavoriteTracks()

        historyTracks.forEach { track ->
            idFavoriteTracks.forEach { id ->
                if (track.trackId == id) {
                    track.isFavorite = true
                }
            }
        }
        emit(historyTracks)
    }.flowOn(Dispatchers.IO).catch { throw IllegalStateException() }

    // запись
    override fun writeHistory(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit().putString(TRACK_KEY, json).apply()
    }
}