package com.example.anuj.TeamManager.controller;

import com.example.anuj.TeamManager.dto.LoginRequest;
import com.example.anuj.TeamManager.dto.RegisterRequest;
import com.example.anuj.TeamManager.model.User;
import com.example.anuj.TeamManager.service.AuthService;
import com.example.anuj.TeamManager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req){
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        return ResponseEntity.ok(authService.validate(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody String refreshToken) {
        return ResponseEntity.ok(authService.userByToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String accessToken){
        return ResponseEntity.ok("User logged out");
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.ok(authService.getUser(email));
    }

}
