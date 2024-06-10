package com.snakeporium_backend.repository;

import com.snakeporium_backend.entity.Order;
import com.snakeporium_backend.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByUserIdAndOrderStatus(Long orderId, OrderStatus status);
}
