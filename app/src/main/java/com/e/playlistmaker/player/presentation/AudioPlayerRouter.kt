package com.e.playlistmaker.player.presentation

import androidx.appcompat.app.AppCompatActivity

class AudioPlayerRouter (private val activity: AppCompatActivity) {
    fun goBack() {
        activity.finish()
    }

}