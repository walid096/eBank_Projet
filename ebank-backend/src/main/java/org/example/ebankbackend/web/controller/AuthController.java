package org.example.ebankbackend.web.controller;

import jakarta.validation.Valid;
import org.example.ebankbackend.service.AuthService;
import org.example.ebankbackend.web.dto.request.ChangePasswordRequest;
import org.example.ebankbackend.web.dto.request.LoginRequest;
import org.example.ebankbackend.web.dto.response.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // ✅ EPIC 1: Change password (authenticated users only)
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.noContent().build(); // 204
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleBadCredentials(BadCredentialsException ex) {
        return ex.getMessage(); // must be "Login ou mot de passe erronés"
    }
}
