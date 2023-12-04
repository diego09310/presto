package diego09310.presto.controllers

import diego09310.presto.services.GameService
import diego09310.presto.services.MessageType
import diego09310.presto.services.WebSocketsService
import org.apache.logging.log4j.LogManager
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

data class BuzzMessage(val playerId: String, val teamId: String)

@Controller
class WebSocketController(
    val gameService: GameService,
    val webSocketsService: WebSocketsService,
) {

    var log = LogManager.getLogger()!!

    @MessageMapping("/buzz")
    fun buzz(message: BuzzMessage) {
        log.debug("Websocket message received: ${message.playerId}, ${message.teamId}")
        gameService.buzz(message.playerId, message.teamId)
    }

}