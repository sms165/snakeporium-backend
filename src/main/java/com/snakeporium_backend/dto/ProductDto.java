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
    private Integer quantity = 1;
//    private String species;
    private Long sexId;
    private String sexName;
    private byte[] byteImg;
    private String imageFormat;
private String latin;
    private Long categoryId;

    private String categoryName;

    private MultipartFile img;
}
