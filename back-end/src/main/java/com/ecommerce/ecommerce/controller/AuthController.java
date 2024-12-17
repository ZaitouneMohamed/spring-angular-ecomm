package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController
{
    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        //return ResponseEntity.ok("Login successful!"); // Static response to test the endpoint

        String token = jwtUtil.generateToken(authRequest.getUsername());
        return ResponseEntity.ok(token); // Return the generated JWT token
    }

    public static class AuthRequest {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
