package com.idearfly.decouple.websocket;

import com.idearfly.decouple.DecoupleConfiguration;
import com.idearfly.decouple.vo.FileSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WsConfiguration implements WebSocketConfigurer {

    public WsConfiguration() {
        DecoupleConfiguration.FileSupport.put(
                "ws",
                new FileSupport(
                        "/images/ws.svg",
                        "/wsManager",
                        "/ws"
                )
        );
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(),"/ws/**")
                .addInterceptors(webSocketHandshakeInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    WebSocketHandler webSocketHandler() {
        return new WebSocketHandler();
    }

    @Bean
    WebSocketHandshakeInterceptor webSocketHandshakeInterceptor() {
        return new WebSocketHandshakeInterceptor();
    }
}
