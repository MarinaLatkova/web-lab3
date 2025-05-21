package org.example.lab3_web.interceptors;

import jakarta.servlet.http.Cookie;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;

public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   org.springframework.web.socket.WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        if (request instanceof org.springframework.http.server.ServletServerHttpRequest servletRequest) {
            Cookie[] cookies = servletRequest.getServletRequest().getCookies();
            if (cookies != null) {
                String accessToken = Arrays.stream(cookies)
                        .filter(c -> "access_token".equals(c.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null);

                if (accessToken != null && !accessToken.isEmpty()) {
                    attributes.put("access_token", accessToken);
                    return true; // Allow handshake
                }
            }
        }

        return false; // Block handshake
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               org.springframework.web.socket.WebSocketHandler wsHandler,
                               Exception exception) {
        // Optional: logging or token processing
    }
}
