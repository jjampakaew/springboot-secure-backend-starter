package com.example.auth.service;

import com.example.auth.security.JwtTokenProvider;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwt;

    public AuthService(AuthenticationManager authManager, JwtTokenProvider jwt) {
        this.authManager = authManager;
        this.jwt = jwt;
    }

    public String login(String email, String password) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        Set<String> roles = auth.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet());

        return jwt.createToken(email, roles);
    }
}