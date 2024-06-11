package com.snakeporium_backend.services.admin.order;


import com.snakeporium_backend.dto.OrderDto;
import com.snakeporium_backend.entity.Order;
import com.snakeporium_backend.enums.OrderStatus;
import com.snakeporium_backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    public List<OrderDto> getAllPlacedOrders() {

        List<Order> orderList = orderRepository.findAllByOrderStatusIn(List.of(OrderStatus.Placed, OrderStatus.Shipped,
                OrderStatus.Delivered));

        return orderList.stream().map(Order::getOrderDto).collect(Collectors.toList());
    }

    public OrderDto changeOrderStatus(Long orderId, String status) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order orderObj = order.get();

            if(Objects.equals(status, "Shipped")){
                orderObj.setOrderStatus(OrderStatus.Shipped);
            }else if(Objects.equals(status, "Delivered")) {
                orderObj.setOrderStatus(OrderStatus.Delivered);
            }
            return orderRepository.save(orderObj).getOrderDto();
        }

        return null;
    }
}
