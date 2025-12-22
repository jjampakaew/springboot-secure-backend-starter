package com.example.auth.config;

import com.example.auth.security.JwtAuthenticationFilter;
import com.example.auth.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   /**
    * AUTH endpoints — no security at all
    */
   @Bean
   @Order(0)
   public SecurityFilterChain authChain(HttpSecurity http) throws Exception {
      http
         .securityMatcher("/auth/**")
         .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
         .csrf(csrf -> csrf.disable())
         .securityContext(sc -> sc.disable())
         .requestCache(cache -> cache.disable())
         .sessionManagement(sm -> sm.disable());

      return http.build();
   }

   /**
    * API endpoints — JWT protected
    */
   @Bean
   @Order(1)
   public SecurityFilterChain apiChain(
         HttpSecurity http,
         JwtAuthenticationFilter jwtFilter
   ) throws Exception {

      http
         .securityMatcher("/api/**")
         .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
         .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
         .csrf(csrf -> csrf.disable())
         .securityContext(sc -> sc.disable())
         .requestCache(cache -> cache.disable())
         .sessionManagement(sm -> sm.disable());

      return http.build();
   }

}

