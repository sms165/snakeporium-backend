package com.snakeporium_backend;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.snakeporium_backend.controller.customer.ProfileController;
import com.snakeporium_backend.dto.UserDto;
import com.snakeporium_backend.services.customer.profile.ProfileService;
import com.snakeporium_backend.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProfileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProfileService profileService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ProfileController profileController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
    }

    @Test
    public void testGetUserProfile() throws Exception {
        // Mocked user ID
        Long userId = 2L;

        // Mocked user DTO
        UserDto mockUserDto = new UserDto();
        mockUserDto.setId(userId);
        mockUserDto.setFirstName("testuser");

        // Mock service method
        when(profileService.getProfileByUserId(userId)).thenReturn(mockUserDto);

        // Perform GET request
        mockMvc.perform(get("/api/customer/profile")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Mocked user data for request and response
        UserDto requestUserDto = new UserDto();

        UserDto responseUserDto = new UserDto();
        responseUserDto.setId(2L); // Example userId

        // Mock service method
        when(profileService.updateProfile(any(Long.class), any(UserDto.class))).thenReturn(responseUserDto);

        // Perform PUT request
        mockMvc.perform(put("/api/customer/profile")
                        .param("userId", "2") // Set userId parameter
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestUserDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2));
    }



    @Test
    public void testUpdatePassword() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(2L); // Set some values to UserDto

        // Mock service method to return userDto
        when(profileService.updatePassword(any(Long.class), any(UserDto.class))).thenReturn(userDto);

        // Perform PUT request
        mockMvc.perform(put("/api/customer/profile/password")
                        .param("userId", "2") // Set userId parameter
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Check for id field, modify as necessary
                .andExpect(jsonPath("$.id").value(2L))
                // Example of checking for non-existence of field
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.role").doesNotExist())
                .andExpect(jsonPath("$.firstName").doesNotExist())
                .andExpect(jsonPath("$.lastName").doesNotExist())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.street").doesNotExist())
                .andExpect(jsonPath("$.streetNumber").doesNotExist())
                .andExpect(jsonPath("$.city").doesNotExist())
                .andExpect(jsonPath("$.state").doesNotExist())
                .andExpect(jsonPath("$.zip").doesNotExist())
                .andExpect(jsonPath("$.phone").doesNotExist());
    }

}
