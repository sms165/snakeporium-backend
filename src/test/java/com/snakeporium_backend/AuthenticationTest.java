package com.snakeporium_backend;

import com.snakeporium_backend.entity.User;
import com.snakeporium_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AuthenticationTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAuthentication() {
        // Create a test user
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        userRepository.save(user);

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");
        assertNotNull(userDetails);

        // Authenticate user
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("test@example.com", "password"));
    }
}