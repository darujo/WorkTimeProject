package ru.darujo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secretKey}")
    private String secretKey;

    public Claims getAllClamsForToken(String token){
        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return Jwts.parser().verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private boolean isTokenExpired (String token){
        try {
            return this.getAllClamsForToken(token).getExpiration().before(new Date());
        }
        catch (ExpiredJwtException ex){
            return true;
        }

    }
    public boolean isInvalid(String token){
        return this.isTokenExpired(token);
    }
}
