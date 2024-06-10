package com.snakeporium_backend.dto;

import lombok.Data;

@Data
public class AddProductInCartDto {

    private Long productId;

    private Long userId;
}
