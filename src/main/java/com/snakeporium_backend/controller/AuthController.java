package com.snakeporium_backend.controller;




import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snakeporium_backend.dto.AuthenticationRequest;
import com.snakeporium_backend.dto.RegisterRequest;
import com.snakeporium_backend.dto.UserDto;
import com.snakeporium_backend.entity.User;
import com.snakeporium_backend.repository.UserRepository;
import com.snakeporium_backend.services.auth.AuthService;
import com.snakeporium_backend.utils.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://snakeporium.yblackbox.com")
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    private final AuthService authService;



    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
        @RequestBody AuthenticationRequest request,
        HttpServletResponse response
    ) throws IOException {
        
        try {
            // 1. Authenticate
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            
            // 2. Load user
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            Optional<User> user = userRepository.findByEmail(request.getUsername());
            
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }
            
            // 3. Generate token
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            
            // 4. Return response
            return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt)
                .body(Map.of(
                    "userId", user.get().getId(),
                    "role", user.get().getRole()
                ));
                
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return new ResponseEntity<>("Test endpoint is working", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (authService.hasUserWithEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto userDto = authService.createUser(registerRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}

