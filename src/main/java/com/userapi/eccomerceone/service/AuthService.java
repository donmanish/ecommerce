package com.userapi.eccomerceone.service;


import com.userapi.eccomerceone.dto.LoginRequest;
import com.userapi.eccomerceone.dto.LoginResponse;

public interface AuthService {

    // Authenticate user and store token in Redis
    LoginResponse login(LoginRequest loginRequest);

    // Logout user and remove token from Redis
    void logout(String userId);

    // Check if token is valid for user
    boolean isAuthenticated(String userId, String token);
}
