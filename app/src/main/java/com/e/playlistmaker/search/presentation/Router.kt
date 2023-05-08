package com.e.playlistmaker.search.presentation

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.e.playlistmaker.player.presentation.AudioPlayerActivity
import com.e.playlistmaker.TRACK

class Router(private val activity: AppCompatActivity) {
    /*companion object {
        private const val TRACK_KEY = "track_model"
    }*/
    fun openTrack(trackId: String) {
        val playerIntent = Intent(activity, AudioPlayerActivity::class.java)
        playerIntent.putExtra(TRACK, trackId)
        activity.startActivity(playerIntent)
    }

    fun goBack() {
        activity.finish()
    }

   /* fun getTrackInfo(): Track {
        return Gson().fromJson((activity.intent.getStringExtra(TRACK)!!), Track::class.java)
    }*/

   /* fun getSharedPreferences(historyPreferences: String, modePrivate: Int): SharedPreferences {
        return getSharedPreferences(historyPreferences, modePrivate)
    }*/
}