package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")
public class DevSecurityConfig {

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
   
   @Bean
   public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
      http
         .authorizeHttpRequests(auth -> auth
               .requestMatchers(
                  "/h2-console/**",
                  "/auth/**"
               ).permitAll()
               .anyRequest().authenticated()
         )
         .csrf(csrf -> csrf.disable())
         .headers(headers -> headers.frameOptions(frame -> frame.disable()));

      return http.build();
   }

   @Bean
   public AuthenticationManager authenticationManager(
         AuthenticationConfiguration config
   ) throws Exception {
      return config.getAuthenticationManager();
   }
}
