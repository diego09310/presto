package diego09310.presto.data

import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials
import java.time.Instant

data class SpotifyUserData(val id: String,
                           var spotifyCredentials: SpotifyAuthorizationCredentials? = null,
                           val email: String)

data class SpotifyAuthorizationCredentials(val accessToken: String,
                                           val tokenType: String,
                                           val scope: String,
                                           val expiresIn: Int,
                                           val tokenExpirationDate: Instant,
                                           var refreshToken: String?) {

    constructor(authorizationCodeCredentials: AuthorizationCodeCredentials): this(
        accessToken = authorizationCodeCredentials.accessToken,
        tokenType = authorizationCodeCredentials.tokenType,
        scope = authorizationCodeCredentials.scope,
        expiresIn = authorizationCodeCredentials.expiresIn,
        tokenExpirationDate = Instant.now().plusSeconds(authorizationCodeCredentials.expiresIn.toLong()),
        refreshToken = authorizationCodeCredentials.refreshToken
    )
}
