package com.e.playlistmaker.player.presentation

interface PlayerScreenView {
    fun goBack()
    fun showTrackCover(image: String)
    fun showTrackName(trackNameText: String)
    fun showArtistName(artistNameText: String)
    fun showTrackTimeText(trackTimeText: String)
    fun goneTrackTimeGroup()
    fun showCollectionNameText(collectionNameText: String)
    fun goneCollectionNameGroup()
    fun showReleaseDateText(releaseDateText: String)
    fun goneReleaseDateGroup()
    fun showGenreNameText(genreNameText: String)
    fun goneGenreNameGroup()
    fun showCountryText(countryText: String)
    fun goneCountryTextGroup()
    fun setEnableButton(b: Boolean)
    fun setImageButton(image: Int)
    fun setProgressTimeText(text: String)
}