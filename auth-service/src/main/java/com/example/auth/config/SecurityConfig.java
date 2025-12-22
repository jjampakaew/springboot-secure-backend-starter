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
@Profile("prod")
public class SecurityConfig {

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   @Order(0)
   public SecurityFilterChain h2ConsoleChain(HttpSecurity http) throws Exception {
      http
         .securityMatcher("/h2-console/**")
         .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
         .csrf(csrf -> csrf.disable())
         .headers(headers -> headers.frameOptions(frame -> frame.disable()))
         .requestCache(cache -> cache.disable());
      return http.build();
   }

   @Bean
   @Order(1)
   public SecurityFilterChain authChain(HttpSecurity http) throws Exception {
      http
         .securityMatcher("/auth/**")
         .csrf(csrf -> csrf.disable())
         .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
      return http.build();
   }

   @Bean
   @Order(2)
   public SecurityFilterChain apiChain(HttpSecurity http, JwtTokenProvider jwt) throws Exception {
      http
         .csrf(csrf -> csrf.disable())
         .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
         .addFilterBefore(
               new JwtAuthenticationFilter(jwt),
               UsernamePasswordAuthenticationFilter.class
         )
         .httpBasic(b -> b.disable());
      return http.build();
   }

   @Bean
   public AuthenticationManager authenticationManager(
      AuthenticationConfiguration config
   ) throws Exception {
      return config.getAuthenticationManager();
   }
}