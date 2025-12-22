package com.example.auth.api;

import com.example.auth.service.AuthService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

   private final AuthService service;

   public AuthController(AuthService service) {
      this.service = service;
   }

   @PostMapping("/login")
   public TokenResponse login(@RequestBody LoginRequest req) {
      String token = service.login(req.email(), req.password());
      return new TokenResponse(token);
   }

   record LoginRequest(@NotBlank String email, @NotBlank String password) {}
   record TokenResponse(String accessToken) {}
}
