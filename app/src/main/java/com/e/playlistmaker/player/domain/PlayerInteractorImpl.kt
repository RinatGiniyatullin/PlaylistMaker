package com.e.playlistmaker.player.domain

import com.e.playlistmaker.search.domain.HistorySearchDataStore
import com.e.playlistmaker.search.domain.Track
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlayerInteractorImpl(
    private val historySearchDataStore: HistorySearchDataStore,
    private val player: Player,
) : PlayerInteractor {

    override suspend fun loadTrack(trackId: String): Flow<Track> = flow {
        val tracks = historySearchDataStore.getHistory().first().first() { track ->
            track.trackId == trackId
        }
        emit(tracks)
    }


    /*  override fun getHistory(): Flow<List<Track>> = flow {
          val json = sharedPreferences.getString(TRACK_KEY, null)
          val historyTracks = Gson().fromJson(json, Array<Track>::class.java).toList()
          val idFavoriteTracks = database.trackDao().getIdFavoriteTracks()
          for (i in historyTracks) {
              for (j in idFavoriteTracks) {
                  if (i.trackId.equals(j)){
                      i.isFavorite = true
                  }
              }
          }
          emit(historyTracks)
      }.flowOn(Dispatchers.IO).catch { throw IllegalStateException() }*/

    override fun playTrack(previewUrl: String) {
        player.play(previewUrl)
    }

    override fun pauseTrack() {
        player.pause()
    }

    override fun getPlayerState(): PlayerState {
        return player.playerState
    }

    override fun getPlayerTime(): Int {
        return player.getCurrentPosition()
    }

    override fun stopPlayer() {
        player.releasePlayer()
    }
}