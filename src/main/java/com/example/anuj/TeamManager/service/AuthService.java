package com.example.anuj.TeamManager.service;

import com.example.anuj.TeamManager.dto.LoginRequest;
import com.example.anuj.TeamManager.dto.RegisterRequest;
import com.example.anuj.TeamManager.model.User;

import java.util.Map;

public interface AuthService {
    Map<String,String> validate(LoginRequest req);
    Map<String,String> register(RegisterRequest req);
    Map<String,String> userByToken(String token);
    User getUser(String email);
}
