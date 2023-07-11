package com.e.playlistmaker.library.ui.favoriteTracks

import com.e.playlistmaker.search.domain.Track

sealed class FavoriteState {

    class FavoriteTracks(val tracks: List<Track>) : FavoriteState()
    object Empty : FavoriteState()
}
