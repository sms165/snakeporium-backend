package com.snakeporium_backend.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sex")
@Data
public class Sex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
