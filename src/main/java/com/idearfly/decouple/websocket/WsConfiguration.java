package com.idearfly.decouple.websocket;

import com.idearfly.decouple.DecoupleConfiguration;
import com.idearfly.decouple.vo.FileSupport;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WsConfiguration {

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
}
