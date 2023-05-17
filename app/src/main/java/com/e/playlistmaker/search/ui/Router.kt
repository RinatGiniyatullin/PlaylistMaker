package com.e.playlistmaker.search.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.e.playlistmaker.player.ui.AudioPlayerActivity
import com.e.playlistmaker.TRACK

class Router(private val activity: AppCompatActivity) {

    fun openTrack(trackId: String) {
        val playerIntent = Intent(activity, AudioPlayerActivity::class.java)
        playerIntent.putExtra(TRACK, trackId)
        activity.startActivity(playerIntent)
    }

    fun goBack() {
        activity.finish()
    }
}