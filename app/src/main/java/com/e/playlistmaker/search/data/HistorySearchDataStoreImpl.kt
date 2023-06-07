package com.e.playlistmaker.search.data

import android.content.SharedPreferences
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.search.domain.HistorySearchDataStore
import com.google.gson.Gson

class HistorySearchDataStoreImpl(private val sharedPreferences: SharedPreferences) :
    HistorySearchDataStore {
    private val TRACK_KEY = "Track_key"

    // Очистка истории
    override fun clearHistory() {
        sharedPreferences.edit().remove(TRACK_KEY).apply()
    }

    // чтение
    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    // запись
    override fun writeHistory(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit().putString(TRACK_KEY, json).apply()
    }
}