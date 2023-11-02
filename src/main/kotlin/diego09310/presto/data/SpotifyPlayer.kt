package diego09310.presto.data

import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext
import se.michaelthelin.spotify.model_objects.specification.Track

enum class PlayState {
    PLAYING,
    PAUSED,
}

data class SpotifyPlayer(var song: String,
                         var artist: String,
                         var cover: String,
                         var playState: PlayState) {

    constructor(currentlyPlayingContext: CurrentlyPlayingContext): this(
        song = currentlyPlayingContext.item.name,
        artist = (currentlyPlayingContext.item as Track).artists[0].name,
        cover = (currentlyPlayingContext.item as Track).album.images.first{it.height > 100 && it.height < 500}.url,
        playState = if (currentlyPlayingContext.is_playing) PlayState.PLAYING else PlayState.PAUSED,
    )
}
