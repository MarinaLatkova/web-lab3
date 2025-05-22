package org.example.lab3_web.binance_integration;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.annotation.PostConstruct;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Service;
import org.java_websocket.client.WebSocketClient;

import java.net.URI;

@Service
public class BinanceWebSocketService {

    private final BinanceWebSocketHandler webSocketHandler;

    public BinanceWebSocketService(BinanceWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @PostConstruct
    public void connectToBinance() {
        try {
            WebSocketClient client = new WebSocketClient(
                    new URI("wss://stream.binance.com:9443/stream?streams=btcusdt@aggTrade/ethusdt@aggTrade/bnbusdt@aggTrade")) {

                @Override
                public void onOpen(ServerHandshake handshakeData) {
                    System.out.println("Connected to Binance");
                }

                @Override
                public void onMessage(String message) {
                    try {
                        JsonObject root = JsonParser.parseString(message).getAsJsonObject();
                        String stream = root.get("stream").getAsString(); // ім'я стріму

                        JsonObject data = root.getAsJsonObject("data");

                        TradeOuterClass.Trade.Builder tradeBuilder = TradeOuterClass.Trade.newBuilder()
                                .setSymbol(data.get("s").getAsString())
                                .setPrice(data.get("p").getAsString())
                                .setTimestamp(data.get("T").getAsLong());

                        TradeOuterClass.Trade tradeProto = tradeBuilder.build();
                        webSocketHandler.broadcastProto(tradeProto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Binance connection closed: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
