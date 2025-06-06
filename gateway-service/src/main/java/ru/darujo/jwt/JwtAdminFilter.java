package ru.darujo.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.ArrayList;

@Component
public class JwtAdminFilter extends JwtAuthFilter {
   @Override
   protected void populateRequestHeader(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClamsForToken(token);
        ArrayList<String> listString = claims.get("authorities", ArrayList.class);
        if(listString==null  || !listString.contains("EDIT_USER".toUpperCase() )){
            throw new RuntimeException("У вас недостаточно прав (EDIT_USER)");
        }

        ServerHttpRequest.Builder builder =
                exchange.getRequest().mutate()
                        .header("username", claims.getSubject());
        listString.forEach(s -> builder.header(s, "true"));
        builder.build();
    }
}
