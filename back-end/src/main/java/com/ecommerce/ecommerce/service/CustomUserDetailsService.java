package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.entity.User;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service  // This annotation ensures that Spring treats this class as a bean
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by username (or email)
        User user = userRepository.findByEmail(username); // If username is email, this works well.

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Convert role to SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // Return user details (with username as email)
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getEmail()); // If using email as unique identifier
        builder.password(user.getPassword());
        builder.authorities(authorities); // Set authorities based on the roles
        return builder.build();
    }
}
