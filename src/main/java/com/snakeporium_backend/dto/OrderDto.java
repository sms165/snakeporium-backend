package com.snakeporium_backend.dto;

import com.snakeporium_backend.entity.CartItems;
import com.snakeporium_backend.entity.User;
import com.snakeporium_backend.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {

    private Long id;

    private String orderDescription;

    private Date date;

    private Double amount;

    private String address;

    private String paymentMethod;

    private OrderStatus orderStatus;

    private Double totalAmount;

    private Double discount;

    private UUID trackingId;


    private String  userName;


    private List<CartItemsDto> cartItems;

}
