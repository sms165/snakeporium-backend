package com.snakeporium_backend.dto;

import com.snakeporium_backend.entity.Order;
import com.snakeporium_backend.entity.Product;
import com.snakeporium_backend.entity.User;
import lombok.Data;

@Data
public class CartItemResponseDto {

    private Product product;
    private Double price;
    private Integer quantity;
    private User user;
//    private Order order;
}
