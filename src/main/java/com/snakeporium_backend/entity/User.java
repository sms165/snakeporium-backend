package com.snakeporium_backend.entity;


import com.snakeporium_backend.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table( name= "users")
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private UserRole role;

    @Lob
    @Column( columnDefinition = "longblob")
    private byte[] image;
}
