package diego09310.presto.services

import diego09310.presto.data.SpotifyPlayer
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.detailed.ForbiddenException
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import java.lang.IllegalStateException

@Service
class SpotifyService(
    private val spotifyAuthenticationService: SpotifyAuthenticationService
) {
    val log = LogManager.getLogger()!!

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

    fun execute(function: () -> Any?): Any? {
        while (true) {
            try {
                return function()
            } catch (ex: IllegalStateException) {
                log.error("Spotify collision, retrying")
                Thread.sleep(100)
            } catch (ex: ForbiddenException) {
                log.error("Tried to play/pause in wrong state")
                return null
            } catch (ex: Exception) {
                log.error("Spotify error: %s", ex)
                return null
            }
        }
    }

    fun getPlayerStatus(): SpotifyPlayer? {
        val playbackInfo = execute { getSpotifyApi()
            ?.informationAboutUsersCurrentPlayback
            ?.build()?.execute() }
        return if (playbackInfo != null) SpotifyPlayer(playbackInfo as CurrentlyPlayingContext) else null
    }

    fun toggle() {
        val isPlaying = getSpotifyApi()?.informationAboutUsersCurrentPlayback?.build()?.execute()?.is_playing ?: return
        if (isPlaying) {
            execute { getSpotifyApi()?.pauseUsersPlayback()?.build()?.execute() }
        } else {
            execute { getSpotifyApi()?.startResumeUsersPlayback()?.build()?.execute() }
        }
    }

    fun startPlayback(playlist: String) {
        execute { getSpotifyApi()?.startResumeUsersPlayback()?.context_uri("spotify:playlist:$playlist")?.build()?.execute() }
        enableShuffle()
    }

    fun enableShuffle() {
        execute { getSpotifyApi()?.toggleShuffleForUsersPlayback(true)?.build()?.execute() }
    }

    fun play() {
        execute { getSpotifyApi()?.startResumeUsersPlayback()?.build()?.execute() }
    }

    fun pause() {
        execute { getSpotifyApi()?.pauseUsersPlayback()?.build()?.execute() }
    }

    fun previous() {
        execute { getSpotifyApi()?.skipUsersPlaybackToPreviousTrack()?.build()?.execute() }
    }

    fun next() {
        execute { getSpotifyApi()?.skipUsersPlaybackToNextTrack()?.build()?.execute() }
    }

    fun getPlaylists(): Array<out PlaylistSimplified>? {
         return getSpotifyApi()?.listOfCurrentUsersPlaylists?.limit(50)?.build()?.execute()?.items
    }
}
