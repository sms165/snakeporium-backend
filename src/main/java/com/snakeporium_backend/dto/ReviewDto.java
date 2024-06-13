package com.snakeporium_backend.dto;


import com.snakeporium_backend.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewDto {


    private Long id;

    private Long rating;


    private String description;

    private String imageFormat;
    private MultipartFile img;
    private byte[] returnedImg;

    private Long userId;


    private Long productId;

    private String username;
}
