package com.userapi.eccomerceone.service;

import com.userapi.eccomerceone.dto.LoginRequest;
import com.userapi.eccomerceone.dto.LoginResponse;
import com.userapi.eccomerceone.model.User;
import com.userapi.eccomerceone.repository.UserRepository;
import com.userapi.eccomerceone.security.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           RedisTemplate<String, Object> redisTemplate,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        String redisKey = "auth:user:" + loginRequest.getEmail();

        // Check Redis first
        Map<String, Object> cachedData = (Map<String, Object>) redisTemplate.opsForValue().get(redisKey);
        if (cachedData != null) {
            LoginResponse cachedResponse = new LoginResponse();
            cachedResponse.setUserId((Long) cachedData.get("userId"));
            cachedResponse.setToken((String) cachedData.get("token"));
            cachedResponse.setMessage((String) cachedData.get("message") + " (from cache)");
            return cachedResponse;
        }

        try {
            // Authenticate user (throws exception if invalid)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Load user from DB
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail());

            // Store token in Redis (with expiration)
            // Store in Redis
            Map<String, Object> loginData = new HashMap<>();
            loginData.put("userId", user.getId());
            loginData.put("token", token);
            loginData.put("message", "Login successful");

            redisTemplate.opsForValue().set(redisKey, loginData, 1, TimeUnit.HOURS);

            // Build response
            LoginResponse response = new LoginResponse();
            response.setUserId(user.getId());
            response.setToken(token);
            response.setMessage("Login successful");

            return response;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public void logout(String userId) {
        redisTemplate.delete("auth:token:" + userId);
    }

    @Override
    public boolean isAuthenticated(String userId, String token) {
        String cachedToken = (String) redisTemplate.opsForValue().get("auth:token:" + userId);
        return cachedToken != null && cachedToken.equals(token);
    }
}
