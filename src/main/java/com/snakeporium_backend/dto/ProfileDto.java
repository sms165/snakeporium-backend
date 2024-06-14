package com.snakeporium_backend.dto;

import com.snakeporium_backend.enums.UserRole;
import lombok.Data;

@Data
public class ProfileDto {
    private Long id;
    //  private String username;
    private String email;
    private UserRole role;
    private String firstName;
    private String lastName;
//    private String password;
    private String street;
    private Long streetNumber;
    private String city;

    private String state;
    private String zip;
    private String phone;

}
