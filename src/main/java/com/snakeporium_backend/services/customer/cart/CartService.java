package com.snakeporium_backend.services.customer.cart;

import com.snakeporium_backend.dto.AddProductInCartDto;
import com.snakeporium_backend.dto.OrderDto;
import com.snakeporium_backend.dto.PlaceOrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {

    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);

    OrderDto getCardByUserId(Long userId);

    OrderDto applyCoupon(Long userId, String code);

    OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto);

    OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto);

    OrderDto placeOrder(PlaceOrderDto placeOrderDto);

    List<OrderDto> getMyPlacedOrders(Long userId);

    OrderDto searchOrderByTrackingId(UUID trackingId);

    ResponseEntity<?> removeItemFromCart(Long productId, Long userId);
//boolean deleteProductFromCart(Long productId, Long userId);

}
