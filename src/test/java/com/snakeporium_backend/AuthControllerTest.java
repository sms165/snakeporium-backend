package com.snakeporium_backend;


import com.snakeporium_backend.controller.AuthController;
import com.snakeporium_backend.dto.AuthenticationRequest;
import com.snakeporium_backend.dto.RegisterRequest;
import com.snakeporium_backend.dto.UserDto;
import com.snakeporium_backend.entity.User;
import com.snakeporium_backend.enums.UserRole;
import com.snakeporium_backend.repository.UserRepository;
import com.snakeporium_backend.services.auth.AuthService;
import com.snakeporium_backend.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateAuthenticationToken_ValidUser() throws Exception {
        // Test setup
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("test@example.com");
        authenticationRequest.setPassword("password");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(authenticationRequest.getUsername(), authenticationRequest.getPassword(), new ArrayList<>());
        User user = new User();
        user.setId(1L);
        user.setEmail(authenticationRequest.getUsername());
        user.setRole(UserRole.CUSTOMER);

        // Mock behavior
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));
        when(userDetailsService.loadUserByUsername(authenticationRequest.getUsername())).thenReturn(userDetails);
        when(userRepository.findByEmail(authenticationRequest.getUsername())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(authenticationRequest.getUsername())).thenReturn("jwtToken");

        // Test action
        MockHttpServletResponse response = new MockHttpServletResponse();
        authController.createAuthenticationToken(authenticationRequest, response);

        // Assertions
        assertEquals(200, response.getStatus());
        assertEquals("{\"role\":\"CUSTOMER\",\"userId\":1}", response.getContentAsString());
        assertEquals("Bearer jwtToken", response.getHeader("Authorization"));
    }



    @Test
    void testCreateAuthenticationToken_InvalidUser() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("test@example.com");
        authenticationRequest.setPassword("password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        MockHttpServletResponse response = new MockHttpServletResponse();
        authController.createAuthenticationToken(authenticationRequest, response);

        assertEquals(401, response.getStatus());
        assertEquals("Invalid username or password", response.getErrorMessage());
    }

    @Test
    void testRegisterUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        when(authService.hasUserWithEmail(registerRequest.getEmail())).thenReturn(false);
        UserDto userDto = new UserDto(); // Assuming you have a constructor that sets the properties
        when(authService.createUser(registerRequest)).thenReturn(userDto);

        ResponseEntity<?> responseEntity = authController.registerUser(registerRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add more assertions if needed for the response entity
    }
}
