package com.e.playlistmaker.player.presentation

interface PlayerScreenView {
    fun goBack()
    fun showTrackCover(image: String)
    fun showTrackName(trackNameText: String)
    fun showArtistName(artistNameText: String)
    fun showTrackTimeText(trackTimeText: String)
    fun hideTrackTimeGroup()
    fun showCollectionNameText(collectionNameText: String)
    fun hideCollectionNameGroup()
    fun showReleaseDateText(releaseDateText: String)
    fun hideReleaseDateGroup()
    fun showGenreNameText(genreNameText: String)
    fun hideGenreNameGroup()
    fun showCountryText(countryText: String)
    fun hideCountryTextGroup()
    fun setEnableButton(b: Boolean)
    fun setImageButton(image: Int)
    fun setProgressTimeText(text: String)
}