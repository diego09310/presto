package diego09310.presto.controllers

import diego09310.presto.data.GameState
import diego09310.presto.services.GameService
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

data class PlayerData(val name: String, val teamId: String, var playerId: String?, var teamName: String?)

@Controller
@RequestMapping("/player")
class PlayerController(
    private val gameService: GameService
) {

    @GetMapping("/login")
    fun getLogin(model: Model): String {
        model["teams"] = gameService.getTeams()
        return "pages/playerLogin"
    }

    @PostMapping("/login")
    fun handleLogin(playerData: PlayerData, session: HttpSession): ModelAndView {
        if (session.getAttribute("playerData") != null) {
            // TODO: Get player and change the name but keep the id. Handle team
            
        }
        val player = gameService.addPlayer(playerData.name, playerData.teamId)
        playerData.playerId = player.id
        playerData.teamName = gameService.getTeam(playerData.teamId)?.name
        session.setAttribute("playerData", playerData)
        if (gameService.getGameState() == GameState.WAITING) {
            return ModelAndView("redirect:/player/waitingRoom")
        }
        return ModelAndView("redirect:/player/button")
    }

    @GetMapping("/waitingRoom")
    fun getWaitingRoom(session: HttpSession, model: Model): ModelAndView {
        if (gameService.getGameState() == GameState.PLAYING) {
            return ModelAndView("redirect:/player/button")
        }
        model["playerData"] = session.getAttribute("playerData")
        model["teams"] = gameService.getTeams()
        return ModelAndView("pages/waitingRoom")
    }

    @GetMapping("/button")
    fun getButton(session: HttpSession, model: Model): String {
        model["playerData"] = session.getAttribute("playerData")
        return "pages/button"
    }

    @GetMapping("/")
    @ResponseBody
    fun getSession(session: HttpSession): String {
        return session.id
    }
}
