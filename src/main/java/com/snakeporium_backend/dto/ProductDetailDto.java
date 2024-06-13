package com.snakeporium_backend.dto;

import com.snakeporium_backend.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailDto {

    private ProductDto productDto;

    private List<ReviewDto> reviewDtoList;

    private List<FAQDto> faqDtoList;
}
