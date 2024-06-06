package com.snakeporium_backend.dto;

import com.snakeporium_backend.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
//    private String username;
    private String email;
    private UserRole role;
    private String firstName;
    private String lastName;

}
