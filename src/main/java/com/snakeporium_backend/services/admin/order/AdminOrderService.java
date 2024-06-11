package com.snakeporium_backend.services.admin.order;

import com.snakeporium_backend.dto.OrderDto;

import java.util.List;

public interface AdminOrderService {

    List<OrderDto> getAllPlacedOrders();

    OrderDto changeOrderStatus(Long orderId, String status);
}
