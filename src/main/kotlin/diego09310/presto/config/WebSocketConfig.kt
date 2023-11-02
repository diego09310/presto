package diego09310.presto.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/ws")
//            .setHandshakeHandler(object : DefaultHandshakeHandler() {
//                @Throws(java.lang.Exception::class)
//                fun beforeHandshake(
//                    request: ServerHttpRequest?,
//                    response: ServerHttpResponse?,
//                    wsHandler: WebSocketHandler?,
//                    attributes: MutableMap<String, Any>
//                ): Boolean {
//                    if (request is ServletServerHttpRequest) {
//                        val session = request
//                            .servletRequest.session
//                        attributes["sessionId"] = session.id
//                    }
//                    return true
//                }
//            })
    }
}