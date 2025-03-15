package ru.darujo.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {
    private JwtUtil jwtUtil;

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public JwtAuthFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!isAuthMissing(request)) {
                final String token = getAuthHeaders(request);
                if (jwtUtil.isInvalid(token)) {
                    return this.onError(exchange, "Не правильный заголовок авторизации", HttpStatus.UNAUTHORIZED);
                }
                populateRequestHeader(exchange, token);
            } else {
                return this.onError(exchange, "Токен отсутсвует", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }

    private void populateRequestHeader(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClamsForToken(token);
        ArrayList<String> listString = claims.get("authorities", ArrayList.class);
        ServerHttpRequest.Builder builder =
                exchange.getRequest().mutate()
                        .header("username", claims.getSubject());
        if (listString != null) {
            listString.forEach(s -> builder.header(s, "true"));
        }
//                .header("authorities",claims.get("authorities",ListString.class))
        builder.build();
    }

    private Mono<Void> onError(ServerWebExchange exchange, String text, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    private String getAuthHeaders(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0).substring(7);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return true;
        }
        return !request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0).startsWith("Bearer");
    }
}
