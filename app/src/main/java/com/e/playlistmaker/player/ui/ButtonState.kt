package com.e.playlistmaker.player.ui

import com.e.playlistmaker.R

sealed class ButtonState(val imageButton: Int) {
    object Play : ButtonState(R.drawable.play)
    object Pause : ButtonState(R.drawable.pause)

}
