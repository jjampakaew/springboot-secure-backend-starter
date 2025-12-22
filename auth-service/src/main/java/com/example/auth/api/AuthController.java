package com.example.auth.api;

import com.example.auth.api.dto.RegisterRequest;
import com.example.auth.service.AuthService;
import com.example.auth.service.UserRegistrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

   private final AuthService authService;
   private final UserRegistrationService userRegistrationService;

   public AuthController(
         AuthService authService,
         UserRegistrationService userRegistrationService
   ) {
      this.authService = authService;
      this.userRegistrationService = userRegistrationService;
   }

   @PostMapping("/register")
   public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

      userRegistrationService.registerUser(
            req.tenantId(),
            req.email(),
            req.password()
      );

      return ResponseEntity.status(HttpStatus.CREATED).build();
   }

   @PostMapping("/login")
   public TokenResponse login(@Valid @RequestBody LoginRequest req) {
      String token = authService.login(req.email(), req.password());
      return new TokenResponse(token);
   }

   record RegisterRequest(
         Long tenantId,
         @NotBlank String email,
         @NotBlank String password
   ) {}

   record LoginRequest(
         @NotBlank String email,
         @NotBlank String password
   ) {}

   record TokenResponse(String accessToken) {}
}