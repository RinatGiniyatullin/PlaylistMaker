package com.e.playlistmaker.search.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.e.playlistmaker.player.presentation.AudioPlayerActivity
import com.e.playlistmaker.TRACK
import com.e.playlistmaker.search.domain.Track

class SearchRouter(private val activity: AppCompatActivity) {

    fun openTrack(trackId: String){
        val playerIntent = Intent(activity, AudioPlayerActivity::class.java)
        playerIntent.putExtra(TRACK, trackId)
        activity.startActivity(playerIntent)
    }

    fun goBack(){
        activity.finish()
    }
}