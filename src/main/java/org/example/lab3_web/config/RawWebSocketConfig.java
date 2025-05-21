package org.example.lab3_web.config;
import org.example.lab3_web.binance_integration.BinanceWebSocketHandler;
import org.example.lab3_web.interceptors.AuthHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class RawWebSocketConfig implements WebSocketConfigurer {

    private final BinanceWebSocketHandler webSocketHandler;

    public RawWebSocketConfig(BinanceWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/binance")
                .addInterceptors(new AuthHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
}

