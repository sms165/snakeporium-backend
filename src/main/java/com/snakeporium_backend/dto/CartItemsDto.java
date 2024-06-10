package com.snakeporium_backend.dto;

import lombok.Data;

@Data
public class CartItemsDto {

    private Long id;

    private Double price;

    private Integer quantity = 1;

    private Long productId;

    private Long orderId;

    private String productName;

    private byte[] returnedImg;

    private Long userId;
}
