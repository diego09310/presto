package diego09310.presto.services

import diego09310.presto.data.SpotifyAuthorizationCredentials
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.SpotifyHttpManager
import java.net.URI
import java.time.Instant

@Component
class SpotifyAuthenticationService(
    @Value("\${SPOTIFY_CLIENT_ID}") private val clientId: String,
    @Value("\${SPOTIFY_CLIENT_SECRET}") private val clientSecret: String,
    @Value("\${SPOTIFY_CALLBACK_URL}") private val callbackUrl: String,
) {
    val log = LogManager.getLogger()!!

    var credentials: SpotifyAuthorizationCredentials? = null

    private val redirectUri: URI = SpotifyHttpManager.makeUri(callbackUrl)

    private var spotifyApi = SpotifyApi.Builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setRedirectUri(redirectUri)
        .build()

    private val authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
        .scope("app-remote-control streaming user-read-playback-state user-modify-playback-state user-read-currently-playing playlist-read-private playlist-read-collaborative")
        .build()

    fun getAuthorizationCodeUri(): String {
        val uri = authorizationCodeUriRequest.execute()
        return uri.toString()
    }

    fun getToken(): String? {
        if (credentials == null) {
            log.error("Spotify authorization missing")
            return null
        }
        if (isTokenExpired()) {
            log.info("Refreshing token")
            refreshAuthorizationCode()
        }
        return credentials!!.accessToken
    }

    fun isTokenExpired(): Boolean {
        return credentials!!.tokenExpirationDate < Instant.now()
    }

    fun authorize(code: String?): Boolean {
        try {
            val authorizationCodeCredentials = spotifyApi.authorizationCode(code).build().execute()
            spotifyApi = SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(authorizationCodeCredentials.refreshToken)
                .build()
            credentials = SpotifyAuthorizationCredentials(authorizationCodeCredentials)
            log.info("Spotify credentials: {}", credentials)
            log.info("Current time {}", Instant.now())
            return true
        } catch (e: Exception) {
            log.error("Error during Spotify authorization: {}", e.message)
        }
        return false
    }

    fun refreshAuthorizationCode() {
        try {
            val authorizationCodeCredentials = spotifyApi.authorizationCodeRefresh().build().execute()
            val refreshToken = credentials!!.refreshToken
            credentials = SpotifyAuthorizationCredentials(authorizationCodeCredentials)
            credentials!!.refreshToken = credentials!!.refreshToken ?: refreshToken
        } catch (e: Exception) {
            log.error("Error during Spotify token refresh: {}", e.message)
        }
    }

}
