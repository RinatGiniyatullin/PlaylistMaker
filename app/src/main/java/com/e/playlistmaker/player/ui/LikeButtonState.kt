package com.e.playlistmaker.player.ui

import com.e.playlistmaker.R

sealed class LikeButtonState(val imageButton: Int) {
    object Like : LikeButtonState(R.drawable.like_ok)
    object DisLike : LikeButtonState(R.drawable.like)
}
