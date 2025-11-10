package ru.darujo.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.ArrayList;

@Component
public abstract class JwtRightFilter extends JwtAuthFilter {
   protected abstract String getRight();
   @Override
   protected void populateRequestHeader(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClamsForToken(token);
        ArrayList<String> listString = claims.get("authorities", ArrayList.class);
        if(listString==null  || !listString.contains(getRight().toUpperCase() )){
            throw new RuntimeException("У вас недостаточно прав (" + getRight() + ")");
        }

        ServerHttpRequest.Builder builder =
                exchange.getRequest().mutate()
                        .header("username", claims.getSubject());
        listString.forEach(s -> builder.header(s, "true"));
        builder.build();
    }
}
