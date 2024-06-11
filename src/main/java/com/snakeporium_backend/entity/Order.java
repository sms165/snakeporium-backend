package com.snakeporium_backend.entity;

import com.snakeporium_backend.dto.OrderDto;
import com.snakeporium_backend.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name= "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<CartItems> cartItems;

    // Update the @ManyToOne mapping for the coupon field
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "coupon_id") // Assuming the column name in the orders table is coupon_id
    private Coupon coupon;

    public OrderDto getOrderDto() {
        OrderDto orderDto = new OrderDto();

        orderDto.setId(id);
        orderDto.setOrderDescription(orderDescription);
        orderDto.setDate(date);
        orderDto.setAmount(amount);
        orderDto.setAddress(address);
        orderDto.setOrderStatus(orderStatus);
        orderDto.setTrackingId(trackingId);
        orderDto.setUserName(user.getFirstName());
        if(coupon != null) {
            orderDto.setCouponName(coupon.getName());
        }
        return orderDto;
    }
}

