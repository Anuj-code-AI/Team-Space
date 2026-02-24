package com.example.anuj.TeamManager.service;


import com.example.anuj.TeamManager.dto.LoginRequest;
import com.example.anuj.TeamManager.dto.RegisterRequest;
import com.example.anuj.TeamManager.exception.InvalidPasswordException;
import com.example.anuj.TeamManager.exception.UserAlreadyExistsException;
import com.example.anuj.TeamManager.exception.UsernameNotFoundException;
import com.example.anuj.TeamManager.model.RefreshToken;
import com.example.anuj.TeamManager.model.Role;
import com.example.anuj.TeamManager.model.User;
import com.example.anuj.TeamManager.repository.RefreshRepository;
import com.example.anuj.TeamManager.repository.UserRepository;
import com.example.anuj.TeamManager.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshRepository refreshRepository;
    private final JwtUtil jwtUtil;

    @Override
    public Map<String,String> validate(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }
        String access = jwtUtil.generateAccessToken(user.getEmail(), user.getRole());
        String refresh = jwtUtil.generateRefreshToken(user.getEmail());
        RefreshToken newToken = new RefreshToken();
        newToken.setToken(refresh);
        newToken.setUser(user);
        newToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshRepository.save(newToken);
        return Map.of(
                "refreshToken",refresh,
                "accessToken",access
        );
    }

    @Override
    public Map<String,String> register(RegisterRequest req) {
        if(userRepository.findByEmail(req.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User already exists");
        }
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        String access = jwtUtil.generateAccessToken(user.getEmail(), user.getRole());
        String refresh = jwtUtil.generateRefreshToken(user.getEmail());
        RefreshToken newToken = new RefreshToken();
        newToken.setToken(refresh);
        newToken.setUser(user);
        newToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshRepository.save(newToken);
        return Map.of(
                "refreshToken",refresh,
                "accessToken",access
        );
    }

    @Override
    public Map<String,String> userByToken(String refreshToken) {

        Claims claims = jwtUtil.parse(refreshToken);
        String email = claims.getSubject();

        RefreshToken stored = refreshRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (stored.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshRepository.delete(stored);
            throw new RuntimeException("Refresh token expired");
        }

        refreshRepository.delete(stored);

        String newRefresh = jwtUtil.generateRefreshToken(email);

        RefreshToken newToken = new RefreshToken();
        newToken.setToken(newRefresh);
        newToken.setUser(stored.getUser());   // <-- mapping to user
        newToken.setExpiryDate(LocalDateTime.now().plusDays(7));

        refreshRepository.save(newToken);

        String newAccess = jwtUtil.generateAccessToken(email, stored.getUser().getRole());

        return Map.of(
                "accessToken", newAccess,
                "refreshToken", newRefresh
        );

    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }
}
