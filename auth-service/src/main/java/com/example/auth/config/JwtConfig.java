package com.example.auth.config;

import com.example.auth.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

   @Bean
   public JwtTokenProvider jwtTokenProvider(
         @Value("${jwt.secret}") String secret,
         @Value("${jwt.validity-seconds}") long validitySeconds
   ) {
      return new JwtTokenProvider(secret, validitySeconds);
   }
}
