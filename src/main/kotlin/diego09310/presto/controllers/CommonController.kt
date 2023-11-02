package diego09310.presto.controllers

import diego09310.presto.services.GameService
import jakarta.websocket.server.PathParam
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/common")
class CommonController(
    val gameService: GameService
) {

    @GetMapping("/teams")
    fun getTeams(@RequestParam host: Boolean?, model: Model): String {
        model["teams"] = gameService.getTeams()
        model["host"] = host
        return "fragments/teams :: #teams"
    }

}