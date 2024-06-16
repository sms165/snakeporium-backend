package com.snakeporium_backend.services.customer.profile;


import com.snakeporium_backend.dto.*;
import com.snakeporium_backend.entity.*;
import com.snakeporium_backend.enums.OrderStatus;
import com.snakeporium_backend.enums.UserRole;
import com.snakeporium_backend.exceptions.ValidationException;
import com.snakeporium_backend.repository.UserRepository;
import com.snakeporium_backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {


    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public UserDto getProfileByUserId(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new ValidationException("User not found with ID: " + userId);
        }

        User user = optionalUser.get(); // Retrieve the user object from Optional

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPassword(user.getPassword());
        userDto.setPhone(user.getPhone());
        userDto.setState(user.getState());
        userDto.setRole(user.getRole());
        userDto.setStreet(user.getStreet());
        userDto.setCity(user.getCity());
        userDto.setZip(user.getZip());
        userDto.setStreetNumber(user.getStreetNumber());

        return userDto;
    }

    public UserDto updatePassword(Long userId, UserDto userDto) {
        // Retrieve the existing user entity from the database
        Optional<User> optionalUser = userRepository.findById(userId);
        System.out.println("Updating password for user with ID: " + userId);
        // Check if the user exists
        if (optionalUser.isPresent()) {
            // Get the existing user entity
            User existingUser = optionalUser.get();

            String newPassword = userDto.getPassword();
            existingUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));


            // Save the updated user entity
            userRepository.save(existingUser);
            System.out.println("Updated password for user with ID " + userId + ": " + existingUser.getPassword());
            // You may want to return an updated UserDto if needed
            return mapUserToUserDto(existingUser);
        } else {
            // Handle case where user is not found
            throw new IllegalArgumentException("User not found for ID: " + userId);
        }
    }




    public UserDto updateProfile(Long userId, UserDto userDto) {
        // Retrieve the existing user entity from the database
        Optional<User> optionalUser = userRepository.findById(userId);

        // Check if the user exists
        if (optionalUser.isPresent()) {
            // Get the existing user entity
            User existingUser = optionalUser.get();

            // Update the fields of the existing user with values from the DTO
            // Use the existing value if the DTO value is null
            String firstName = userDto.getFirstName() != null ? userDto.getFirstName() : existingUser.getFirstName();
            String lastName = userDto.getLastName() != null ? userDto.getLastName() : existingUser.getLastName();
            String city = userDto.getCity() != null ? userDto.getCity() : existingUser.getCity();
            String state = userDto.getState() != null ? userDto.getState() : existingUser.getState();
            String street = userDto.getStreet() != null ? userDto.getStreet() : existingUser.getStreet();
            Long streetNumber = userDto.getStreetNumber() != null ? userDto.getStreetNumber() : existingUser.getStreetNumber();
            String zip = userDto.getZip() != null ? userDto.getZip() : existingUser.getZip();
            String phone = userDto.getPhone() != null ? userDto.getPhone() : existingUser.getPhone();

            // Update the existing user with the new or existing values
            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setCity(city);
            existingUser.setState(state);
            existingUser.setStreet(street);
            existingUser.setStreetNumber(streetNumber);
            existingUser.setZip(zip);
            existingUser.setPhone(phone);

            // Validate required fields if necessary
            if (firstName == null || lastName == null) {
                throw new IllegalArgumentException("First name and last name are required fields.");
            }

            // Save the updated user entity
            User updatedUser = userRepository.save(existingUser);

            // Convert the updated user entity to DTO and return
            return mapUserToUserDto(updatedUser);
        } else {
            throw new IllegalArgumentException("User not found for ID: " + userId);
        }
    }

    // Helper method to map User entity to UserDto
    private UserDto mapUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setCity(user.getCity());
        userDto.setState(user.getState());
        userDto.setStreet(user.getStreet());
        userDto.setStreetNumber(user.getStreetNumber());
        userDto.setZip(user.getZip());
        userDto.setPhone(user.getPhone());
        userDto.setPassword(user.getPassword());
        // Map other fields as needed

        return userDto;
    }



}
