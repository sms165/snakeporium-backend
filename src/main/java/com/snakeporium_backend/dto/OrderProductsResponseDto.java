package com.snakeporium_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderProductsResponseDto {

    private List<ProductDto> productDtoList;

    private Double orderAmount;
}
