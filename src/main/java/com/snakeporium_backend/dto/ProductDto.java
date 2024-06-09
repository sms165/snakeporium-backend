package com.snakeporium_backend.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ProductDto {

    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String species;
    private String sex;
    private byte[] byteImg;
    private String imageFormat;

    private Long categoryId;

    private String categoryName;

    private MultipartFile img;
}
