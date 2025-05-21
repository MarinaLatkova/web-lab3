package org.example.lab3_web.binance_integration;

import org.example.lab3_web.TradeProto;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BinanceWebSocketHandler implements WebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("WebSocket connected: " + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        // Not used — server only pushes Binance data
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.err.println("WebSocket error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        sessions.remove(session);
        System.out.println("WebSocket disconnected: " + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void broadcastProto(TradeProto.Trade trade) {
        byte[] data = trade.toByteArray();
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new BinaryMessage(data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
