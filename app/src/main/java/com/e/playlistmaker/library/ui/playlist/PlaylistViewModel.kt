package com.e.playlistmaker.library.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.e.playlistmaker.player.ui.DateTimeFormatter
import com.e.playlistmaker.search.domain.Track
import com.e.playlistmaker.sharing.domain.SharingInteractor
import createMinutesCountString
import createTracksCountString
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val interactor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    private val _playlistInfo = MutableLiveData<PlaylistInfo>()
    val playlistInfo: LiveData<PlaylistInfo> = _playlistInfo

    private val _isPlaylistDeleted = MutableLiveData<Boolean>()
    val isPlaylistDeleted: LiveData<Boolean> = _isPlaylistDeleted

    fun loadPlaylist(playlistId: Int) {

        var cover = ""
        var title = ""
        var description = ""
        var time = ""
        var tracksCount = ""
        val listTracks = mutableListOf<Track>()

        viewModelScope.launch {
            val playlist = interactor.getPlaylistById(playlistId)
            cover = playlist.uriForImage
            title = playlist.title
            description = playlist.description

            listTracks.addAll(interactor.getTracksForPlaylist(playlist.tracksId))

            time = createTimeValue(listTracks)

            tracksCount = createTracksValue(listTracks.size)

            val visibilityForEmptyTracks = listTracks.size != 0

            _playlistInfo.postValue(
                PlaylistInfo.Info(
                    cover,
                    title,
                    description,
                    time,
                    tracksCount,
                    listTracks,
                    visibilityForEmptyTracks
                )
            )
        }
    }

    private fun createTimeValue(listTracks: List<Track>): String {
        var timeAllTracks = 0L
        for (track in listTracks) {
            timeAllTracks += track.trackTimeMillis
        }

        val value = SimpleDateFormat(
            "mm",
            Locale.getDefault()
        ).format(timeAllTracks).toInt()

        return when (value) {
            0 -> ""
            else -> createMinutesCountString(value)
        }
    }

    private fun createTracksValue(size: Int): String {
        return if (size == 0) {
            "Треков нет"
        } else {
            createTracksCountString(size)
        }
    }

    fun deleteTrack(trackId: String, playlistId: Int) {

        viewModelScope.launch {
            interactor.deleteTrack(trackId, playlistId).collect { loadPlaylist((it)) }
        }
    }

    private fun formateTime(trackTimeMillis: Long): String {
        return SimpleDateFormat(
            DateTimeFormatter.PROGRESS_FORMAT, Locale.getDefault()
        ).format(trackTimeMillis)
    }

    fun sharePlaylist(
        title: CharSequence?,
        description: CharSequence?,
        numberOfTracks: CharSequence?,
        listTracks: MutableList<Track>,
    ) {
        var tracks = ""
        var position = 0
        for (track in listTracks) {
            position += 1
            tracks =
                if (tracks.isNotEmpty()) "$tracks\n${
                    newTrack(
                        position,
                        track.artistName,
                        track.trackName,
                        track.trackTimeMillis
                    )
                }"
                else newTrack(
                    position,
                    track.artistName,
                    track.trackName,
                    track.trackTimeMillis
                )
        }

        val message = "$title\n${
            if (description!!.isNotEmpty())
                "$description\n$numberOfTracks:\n" +
                        "$tracks"
            else "$numberOfTracks:\n$tracks"
        }"

        sharingInteractor.shareAppWithMessage(message)
    }

    private fun newTrack(
        position: Int,
        artistName: String,
        trackName: String,
        trackTimeMillis: Long,
    ): String {
        return "$position. ${artistName} - ${trackName} (${formateTime(trackTimeMillis)})"
    }

    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            interactor.deletePlaylist(playlistId).collect { result ->
                _isPlaylistDeleted.postValue(result)
            }
        }
    }
}