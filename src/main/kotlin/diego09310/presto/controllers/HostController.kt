package diego09310.presto.controllers

import diego09310.presto.services.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/host")
class HostController(
    private val gameService: GameService,
    private val spotifyAuthenticationService: SpotifyAuthenticationService,
    private val spotifyService: SpotifyService,
    private val webSocketsService: WebSocketsService,
) {

    // TODO: Check spring security
//    @GetMapping("/login")
//    fun getLogin()

    @GetMapping("/spotifyLogin")
    fun getSpotifyLogin(): String {
        return "pages/spotifyLogin"
    }

    @PostMapping("/spotifyLogin")
    fun handleSpotifyLogin(): ModelAndView {
        val authUrl = spotifyAuthenticationService.getAuthorizationCodeUri()
        return ModelAndView("redirect:$authUrl")
    }

    @GetMapping("/spotifyCallback")
    fun getSpotifyCallback(@RequestParam code: String): ModelAndView {
        if (spotifyAuthenticationService.authorize(code)) {
            return ModelAndView("redirect:/host/dashboard")
        }
        return ModelAndView("error") // TODO: ErrorPage
    }

    @GetMapping("/dashboard")
    fun getDashboard(model: Model): ModelAndView {
        if (spotifyAuthenticationService.credentials == null) {
            return ModelAndView("redirect:/host/spotifyLogin")
        }
        model["teams"] = gameService.getTeams()
        model["host"] = true
        model["spotify"] = spotifyService.getPlayerStatus()
        model["playlists"] = spotifyService.getPlaylists()
        return ModelAndView("pages/dashboard")
    }

    @GetMapping("/addTeam")
    @ResponseBody
    fun addTeam(@RequestParam teamName: String) {
        gameService.addTeam(teamName)
    }

    @GetMapping("/deleteTeam")
    @ResponseBody
    fun deleteTeam(@RequestParam teamId: String) {
        gameService.deleteTeam(teamId)
    }

    @GetMapping("/deletePlayer")
    @ResponseBody
    fun deletePlayer(@RequestParam playerId: String, @RequestParam teamId: String) {
        gameService.deletePlayer(playerId, teamId)
    }

    @GetMapping("/game/start")
    @ResponseBody
    fun startGame() {
        gameService.startGame()
    }

    @GetMapping("/game/pause")
    @ResponseBody
    fun pauseGame() {
        webSocketsService.sendAll(MessageType.GAME.type, "pause")
    }

}
