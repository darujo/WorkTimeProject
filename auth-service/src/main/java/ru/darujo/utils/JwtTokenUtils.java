package ru.darujo.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.darujo.model.User;

import java.util.*;


@Component
public class JwtTokenUtils {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.lifeTimeToken}")
    private Integer lifeTimeToken;

    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getNikName());
        if (userDetails.getCurrentProject() != null) {
            claims.put("project", userDetails.getCurrentProject().getId());
            claims.put("project_code", userDetails.getCurrentProject().getCode());
        }
        Set<String> rightList = new HashSet<>();
        if (userDetails.getRights() != null) {
            userDetails.getRights()
                    .forEach(right -> rightList.add(
                            right.getName()));
        }
        if (userDetails.getRoles() != null) {
            userDetails.getRoles().stream().filter(role -> role.getProject().equals(userDetails.getCurrentProject()))
                    .forEach(role ->
                            role.getRights().forEach(right -> rightList.add(
                                    right.getName()))
                    );
        }
        if (!rightList.isEmpty()) {
            claims.put("authorities", rightList);
        }
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifeTimeToken);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getNikName())
                .setExpiration(expiredDate)
                .setIssuedAt(issuedDate)

                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
