package com.example.auth.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

   private final JwtTokenProvider jwt;

   public JwtAuthenticationFilter(JwtTokenProvider jwt) {
      this.jwt = jwt;
   }

   @Override
   protected boolean shouldNotFilter(HttpServletRequest request) {
      String path = request.getRequestURI();
      return path.startsWith("/auth/");
   }

   @Override
   protected void doFilterInternal(
         HttpServletRequest request,
         HttpServletResponse response,
         FilterChain filterChain
   ) throws ServletException, IOException {

      String header = request.getHeader("Authorization");

      if (header != null && header.startsWith("Bearer ")) {
         String token = header.substring(7);
         try {
               Claims claims = jwt.parse(token).getBody();

               String subject = claims.getSubject();
               List<String> roles = claims.get("roles", List.class);

               var authorities = roles.stream()
                     .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                     .collect(Collectors.toSet());

               var auth = new UsernamePasswordAuthenticationToken(
                     subject, null, authorities
               );

               SecurityContextHolder.getContext().setAuthentication(auth);
         } catch (Exception ignored) {
               // invalid token → no auth
         }
      }

      filterChain.doFilter(request, response);
   }
}

