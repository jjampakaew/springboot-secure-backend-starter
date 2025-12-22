package com.example.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

public class JwtTokenProvider {

   private final Key key;
   private final long validitySeconds;

   public JwtTokenProvider(String secret, long validitySeconds) {
      this.key = Keys.hmacShaKeyFor(secret.getBytes());
      this.validitySeconds = validitySeconds;
   }

   public String createToken(String subject, Set<String> roles) {
      Instant now = Instant.now();
      return Jwts.builder()
               .setSubject(subject)
               .claim("roles", roles)
               .setIssuedAt(Date.from(now))
               .setExpiration(Date.from(now.plusSeconds(validitySeconds)))
               .signWith(key, SignatureAlgorithm.HS256)
               .compact();
   }

   public Jws<Claims> parse(String token) {
      return Jwts.parserBuilder()
               .setSigningKey(key)
               .build()
               .parseClaimsJws(token);
   }
}
