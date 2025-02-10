package ru.darujo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtils {
   @Value("${jwt.secretKey}")
   private String secretKey;
   @Value("${jwt.lifeTimeToken}")
   private Integer lifeTimeToken;
//   private <T> T getClaimFormToken(String token, Function<Claims,T> callback){
//      Claims claims =getAllClamsForToken(token);
//      return callback.apply(claims);
//   }
//
//   private Claims getAllClamsForToken(String token){
//      return Jwts.parser()
//              .setSigningKey(secretKey)
//              .parseClaimsJws(token)
//              .getBody();
//   }
//   public String getUsername(String token) {
//      return getClaimFormToken(token,  claims -> claims.get("username", String.class));
//   }

   public  String generateToken(UserDetails userDetails){
      Map<String,Object> claims = new HashMap<>();
      claims.put("username",userDetails.getUsername());
      Date issuedDate  = new Date();
      Date expiredDate = new Date(issuedDate.getTime() + lifeTimeToken);
      return Jwts.builder()
              .setClaims(claims)
              .setSubject(userDetails.getUsername())
              .setExpiration(expiredDate)
              .setIssuedAt(issuedDate)
              .signWith(SignatureAlgorithm.HS256,secretKey)
              .compact();
   }
}
