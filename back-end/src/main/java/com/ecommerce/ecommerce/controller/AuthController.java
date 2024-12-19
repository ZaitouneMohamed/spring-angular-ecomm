package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.entity.User;
import com.ecommerce.ecommerce.enums.UserRole;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        logger.info("Register request received for email: {}", authRequest.getEmail());

        if (authRequest.getEmail() == null || authRequest.getPassword() == null) {
            logger.warn("Registration failed: Email or password is null.");
            return ResponseEntity.badRequest().body("Email or password cannot be null");
        }

        if (userRepository.findByEmail(authRequest.getEmail()) != null) {
            logger.warn("Registration failed: User with email {} already exists.", authRequest.getEmail());
            return ResponseEntity.badRequest().body("User already exists");
        }

        User newUser = new User();
        newUser.setEmail(authRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        newUser.setRole(UserRole.CUSTOMER);
        userRepository.save(newUser);

        logger.info("User with email {} registered successfully.", authRequest.getEmail());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        logger.info("Login request received for email: {}", authRequest.getEmail());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword());

        try {
            Authentication auth = authenticationManager.authenticate(authToken);
            if (auth.isAuthenticated()) {
                String token = jwtUtil.generateToken(authRequest.getEmail());
                logger.info("User {} logged in successfully. Token generated.", authRequest.getEmail());
                return ResponseEntity.ok(token);
            }
        } catch (Exception e) {
            logger.error("Authentication failed for user {}: {}", authRequest.getEmail(), e.getMessage());
        }

        logger.warn("Login failed for user {}: Invalid credentials.", authRequest.getEmail());
        return ResponseEntity.status(403).body("Forbidden: Invalid credentials");
    }


    public static class AuthRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
