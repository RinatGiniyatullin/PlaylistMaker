package com.e.playlistmaker.player.ui

import com.e.playlistmaker.R

sealed class ButtonState(val imageButton: Int) {
    object Play : ButtonState(R.drawable.play)
    object Pause : ButtonState(R.drawable.pause)

    object Like : ButtonState(R.drawable.like_ok)
    object DisLike : ButtonState(R.drawable.like)
}
