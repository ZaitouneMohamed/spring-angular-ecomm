package com.ecommerce.ecommerce.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // Secret key for signing the JWT token (keep this safe, ideally in an environment variable)
    private final String secretKey = "vkvHG0uRAcJoniVkbyAzr2ZCTogWVS3pQ0DRrdLTEEd8AEUagmZfJ1lgXz6p90m5p+nfxJ4X2UHHAbluQPt+8g==";

    // Generate JWT token based on the username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // Username as the subject of the token
                .setIssuedAt(new Date())  // Set the current time as the issued time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // Token expiration time (10 hours)
                .signWith(SignatureAlgorithm.HS256, secretKey)  // Sign the token using HS256 algorithm and secret key
                .compact();  // Generate the token
    }

    // Extract the username from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);  // Extract the 'subject' claim (which is the username)
    }

    // Extract a specific claim (like expiration) from the token
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);  // Extract all claims
        return claimsResolver.apply(claims);  // Resolve the specific claim (e.g., expiration)
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()  // Use parser() for JJWT 0.12.6
                .setSigningKey(secretKey)  // Set the signing key for verification
                .build()
                .parseClaimsJws(token)  // Parse the JWT token and get the claims
                .getBody();  // Return the claims body
    }

    // Check if the JWT token is expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());  // Compare the expiration date with the current date
    }

    // Extract the expiration date from the JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);  // Extract the 'expiration' claim from the token
    }

    // Validate the JWT token by checking if it's expired and the username matches
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = userDetails.getUsername();  // Extract username from UserDetails
        return username.equals(extractUsername(token)) && !isTokenExpired(token);  // Validate token
    }
}
