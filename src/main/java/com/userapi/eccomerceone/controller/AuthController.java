package com.userapi.eccomerceone.controller;

import com.userapi.eccomerceone.dto.LoginRequest;
import com.userapi.eccomerceone.dto.LoginResponse;
import com.userapi.eccomerceone.model.User;
import com.userapi.eccomerceone.security.JwtUtil;
import com.userapi.eccomerceone.service.AuthService;
import com.userapi.eccomerceone.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and logout APIs")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService,UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }



    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        User userResponse = userService.register(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("data", userResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Could be BadCredentialsException or user not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Login failed", e.getMessage()) {
                    });
        }
    }

    @PostMapping("/logout/{userId}")
    @Operation(summary = "Logout a user")
    public ResponseEntity<Object> logout(@PathVariable String userId) {
        try {
            authService.logout(userId);
            return ResponseEntity.ok(new SuccessResponse("Logout successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Logout failed", e.getMessage()));
        }
    }

    // Inner classes for structured responses
    static class ErrorResponse {
        private String message;
        private String error;

        public ErrorResponse(String message, String error) {
            this.message = message;
            this.error = error;
        }
        public String getMessage() { return message; }
        public String getError() { return error; }
    }

    static class SuccessResponse {
        private String message;
        public SuccessResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}
