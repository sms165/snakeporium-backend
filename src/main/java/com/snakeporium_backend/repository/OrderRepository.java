package com.snakeporium_backend.repository;

import com.snakeporium_backend.entity.Order;
import com.snakeporium_backend.enums.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByTrackingId(UUID trackingId);

    Order findByUserIdAndOrderStatus(Long orderId, OrderStatus status);

    List<Order> findAllByOrderStatusIn(List<OrderStatus> orderStatusList);

    List<Order> findByUserIdAndOrderStatusIn(Long orderId,List<OrderStatus> status);

    Optional<Order> findByTrackingId(UUID trackingId);

    List<Order> findAllByDateBetweenAndOrderStatus(Date startOfMonth, Date endOfMonth, OrderStatus orderStatus);

    Long countByOrderStatus(OrderStatus orderStatus);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.cartItems WHERE o.user.id = :userId AND o.orderStatus = :orderStatus")
Order findByUserIdAndOrderStatusWithCartItems(Long userId, OrderStatus orderStatus);

}
