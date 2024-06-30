package com.snakeporium_backend.controller.customer;


import com.snakeporium_backend.dto.AddProductInCartDto;
import com.snakeporium_backend.dto.OrderDto;
import com.snakeporium_backend.dto.PlaceOrderDto;
import com.snakeporium_backend.entity.Order;
import com.snakeporium_backend.exceptions.ValidationException;
import com.snakeporium_backend.services.customer.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductInCartDto addProductInCartDto) {
        return cartService.addProductToCart(addProductInCartDto);
    }

    @GetMapping("/cart/{userId}")
    public ResponseEntity<?> addCartByUserId(@PathVariable Long userId) {
        OrderDto orderDto = cartService.getCardByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }


    @DeleteMapping("/cart/{userId}/{productId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long userId, @PathVariable Long productId) {


        try {


            ResponseEntity<?> orderDto = cartService.removeItemFromCart(productId, userId);
            if (orderDto != null) {
                return ResponseEntity.status(HttpStatus.OK).body(orderDto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in the cart");
            }
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


    @GetMapping("/coupon/{userId}/{code}")
    public ResponseEntity<?> applyCoupon(@PathVariable Long userId, @PathVariable String code) {
        try {
            OrderDto orderDto = cartService.applyCoupon(userId, code);
            return ResponseEntity.status(HttpStatus.OK).body(orderDto);
        }catch (ValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    }

    @PostMapping("/addition")
    public ResponseEntity<OrderDto> increaseProductQuanity(@RequestBody AddProductInCartDto addProductInCartDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.increaseProductQuantity(addProductInCartDto));
    }

    @PostMapping("/deduction")
    public ResponseEntity<OrderDto> decreaseProductQuanity(@RequestBody AddProductInCartDto addProductInCartDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.decreaseProductQuantity(addProductInCartDto));
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderDto placeOrderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.placeOrder(placeOrderDto));
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<OrderDto>> getMyPlacedOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getMyPlacedOrders(userId));
    }
}
