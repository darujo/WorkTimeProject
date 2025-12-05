package ru.darujo.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class JwtTokenUtils {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.lifeTimeToken}")
    private Integer lifeTimeToken;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        Set<String> listString = new HashSet<>();
        userDetails.getAuthorities()
                .forEach(grantedAuthority -> listString.add(
                        grantedAuthority.getAuthority()));
        if (!listString.isEmpty()) {
            claims.put("authorities", listString);
        }
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifeTimeToken);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setExpiration(expiredDate)
                .setIssuedAt(issuedDate)

                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
