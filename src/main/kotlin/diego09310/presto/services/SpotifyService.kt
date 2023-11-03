package diego09310.presto.services

import diego09310.presto.data.SpotifyPlayer
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified

@Service
class SpotifyService(
    private val spotifyAuthenticationService: SpotifyAuthenticationService
) {
    private lateinit var spotifyApi: SpotifyApi

    private fun getSpotifyApi(): SpotifyApi? {
        if (this::spotifyApi.isInitialized && !spotifyAuthenticationService.isTokenExpired()) {
            return spotifyApi
        }
        val token = spotifyAuthenticationService.getToken()
        if (!this::spotifyApi.isInitialized && token == null) {
            return null
        }
        spotifyApi = SpotifyApi.builder()
            .setAccessToken(token)
            .build()
        return spotifyApi
    }

    fun getPlayerStatus(): SpotifyPlayer? {
        val playbackInfo = getSpotifyApi()
            ?.informationAboutUsersCurrentPlayback
            ?.build()?.execute()
        return if (playbackInfo != null) SpotifyPlayer(playbackInfo) else null
    }

    fun toggle() {
        val isPlaying = getSpotifyApi()?.informationAboutUsersCurrentPlayback?.build()?.execute()?.is_playing ?: return
        if (isPlaying) {
            getSpotifyApi()?.pauseUsersPlayback()?.build()?.execute()
        } else {
            getSpotifyApi()?.startResumeUsersPlayback()?.build()?.execute()
        }
    }

    fun startPlayback(playlist: String) {
        getSpotifyApi()?.startResumeUsersPlayback()?.context_uri("spotify:playlist:$playlist")?.build()?.execute()
        enableShuffle()
    }

    fun enableShuffle() {
        getSpotifyApi()?.toggleShuffleForUsersPlayback(true)?.build()?.execute()
    }

    fun play() {
        val isPlaying = getSpotifyApi()?.informationAboutUsersCurrentPlayback?.build()?.execute()?.is_playing ?: return
        if (!isPlaying) {
            getSpotifyApi()?.startResumeUsersPlayback()?.build()?.execute()
        }
    }

    fun pause() {
        val isPlaying = getSpotifyApi()?.informationAboutUsersCurrentPlayback?.build()?.execute()?.is_playing ?: return
        if (isPlaying) {
            getSpotifyApi()?.pauseUsersPlayback()?.build()?.execute()
        }
    }

    fun previous() {
        getSpotifyApi()?.skipUsersPlaybackToPreviousTrack()?.build()?.execute()
    }

    fun next() {
        getSpotifyApi()?.skipUsersPlaybackToNextTrack()?.build()?.execute()
    }

    fun getPlaylists(): Array<out PlaylistSimplified>? {
        return getSpotifyApi()?.listOfCurrentUsersPlaylists?.limit(50)?.build()?.execute()?.items
    }
}
