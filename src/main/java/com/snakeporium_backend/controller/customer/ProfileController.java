package com.snakeporium_backend.controller.customer;


import com.snakeporium_backend.dto.AnalyticsResponse;
import com.snakeporium_backend.dto.ProductDto;
import com.snakeporium_backend.dto.ProfileDto;
import com.snakeporium_backend.dto.UserDto;
import com.snakeporium_backend.entity.User;
import com.snakeporium_backend.exceptions.ValidationException;
import com.snakeporium_backend.services.customer.profile.ProfileService;
import com.snakeporium_backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/customer")

public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ProfileController(ProfileService profileService, JwtUtil jwtUtil) {
        this.profileService = profileService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(@RequestParam Long userId) {
        UserDto userDto = profileService.getProfileByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateUser(@RequestParam Long userId, @RequestBody UserDto userDto) throws IOException {



        System.out.println("Received userId: " + userId);
        System.out.println("Received UserDto: " + userDto);

        // Update user
        UserDto updatedUser = profileService.updateProfile(userId, userDto);

        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


//    @GetMapping("/profile")
//    public ResponseEntity<UserDto> getUserProfile() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String token = (String) authentication.getCredentials(); // Assuming token is stored in credentials
//        System.out.println("Received JWT token: " + token);
//        logger.info("Received JWT token: " + token); // Log the token
//
//        if (token == null || token.trim().isEmpty()) {
//            logger.error("JWT token is missing or empty");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        try {
//            String userId = jwtUtil.extractUserId(token); // Extract userId from the token
//            Long userIdLong = Long.parseLong(userId); // Convert userId to Long if needed
//
//            UserDto userDto = profileService.getUserProfile(userIdLong);
//
//            if (userDto != null) {
//                return ResponseEntity.ok(userDto);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            logger.error("Error processing JWT token", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}
//
//    @GetMapping("/profile")
//    public ResponseEntity<UserDto> getUserProfile() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("Authentication: " + authentication);
//
//// Extract the username (which contains the user ID in your case)
//        String username = authentication.getName();
//        System.out.println("Username extracted from authentication: " + username);
//
//// Assuming JwtUtil can extract the user ID from the JWT token
//        public Long extractAllClaims(String token) {
//            return ;
//        }
//
//// Convert user ID string to Long
//        Long userId = Long.valueOf(userIdString);
//        System.out.println("User ID as Long: " + userId);
//
//// Call profile service to fetch user profile
//        UserDto userDto = profileService.getProfileByUserId(userId);
//        System.out.println("User DTO fetched from profile service: " + userDto);
//
//
//        // Return ResponseEntity with userDto in the body
//        return ResponseEntity.status(HttpStatus.OK).body(userDto);
//    }

//    @GetMapping("/profile")
//    public ResponseEntity<UserDto> getUserProfile() {
//        UserDto userDto = profileService.getProfileByUserId(userId);
//        return ResponseEntity.status(HttpStatus.OK).body(userDto);
//    }

}