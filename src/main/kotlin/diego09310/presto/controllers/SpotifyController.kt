package diego09310.presto.controllers

import diego09310.presto.data.SpotifyPlayer
import diego09310.presto.services.SpotifyService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified

@Controller
@RequestMapping("/spotify")
class SpotifyController(val spotifyService: SpotifyService) {

    @GetMapping("/toggle")
    @ResponseBody
    fun toggle() {
        spotifyService.toggle()
    }

    @GetMapping("/prev")
    @ResponseBody
    fun prev() {
        spotifyService.previous()
    }

    @GetMapping("/next")
    @ResponseBody
    fun next() {
        spotifyService.next()
    }

    @GetMapping("/status")
    @ResponseBody
    fun getPlayerStatus(): SpotifyPlayer? {
        return spotifyService.getPlayerStatus()
    }
}