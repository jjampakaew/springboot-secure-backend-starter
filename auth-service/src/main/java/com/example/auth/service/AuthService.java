package com.example.auth.service;

import com.example.auth.security.JwtTokenProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

   private final UserDetailsService userDetailsService;
   private final PasswordEncoder passwordEncoder;
   private final JwtTokenProvider jwt;

   public AuthService(
         UserDetailsService userDetailsService,
         PasswordEncoder passwordEncoder,
         JwtTokenProvider jwt
   ) {
      this.userDetailsService = userDetailsService;
      this.passwordEncoder = passwordEncoder;
      this.jwt = jwt;
   }

   public String login(String email, String password) {

      UserDetails user = userDetailsService.loadUserByUsername(email);

      if (!passwordEncoder.matches(password, user.getPassword())) {
         throw new BadCredentialsException("Invalid credentials");
      }

      Set<String> roles = user.getAuthorities().stream()
               .map(a -> a.getAuthority().replace("ROLE_", ""))
               .collect(Collectors.toSet());

      return jwt.createToken(user.getUsername(), roles);
   }
}
