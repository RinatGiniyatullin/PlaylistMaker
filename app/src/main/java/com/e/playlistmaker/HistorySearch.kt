package com.e.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class HistorySearch(private val sharedPreferences: SharedPreferences) {

    // Очистка истории
    fun clearHistory() {
        sharedPreferences.edit().remove(TRACK_KEY).apply()
    }

    // чтение
    fun readFromJson(): Array<ITunesAudio> {
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<ITunesAudio>::class.java)
    }

    // запись
    fun writeToJson(tracks: ArrayList<ITunesAudio>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit().putString(TRACK_KEY, json).apply()
    }
}