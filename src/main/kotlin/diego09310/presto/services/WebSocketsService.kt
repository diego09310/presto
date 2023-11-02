package diego09310.presto.services

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

data class WebSocketMessage(val type: String, val data: Any?)

enum class MessageType(val type: String) {
    GAME("game"),
    NEW_TEAM("newTeam"),
    NEW_USER("newUser"),
    BUZZ_RESULTS("buzzResults"),
}

@Component
class WebSocketsService(
    val template: SimpMessagingTemplate
) {

    fun sendAll(type: String, message: Any?) {
        this.template.convertAndSend("/topic/all", WebSocketMessage(type, message))
    }

    fun sendSpotify(type: String, message: Any) {
        this.template.convertAndSend("/topic/spotify", WebSocketMessage(type, message))
    }

    // New user added
    // New team added
    // Game starts (button)
    // Disable button after click
    // Enable after resume
    // Toast al dashboard error spotify no conectado
    // Styling
}