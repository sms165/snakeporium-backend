package com.snakeporium_backend.controller.customer;


import com.snakeporium_backend.dto.AddProductInCartDto;
import com.snakeporium_backend.dto.OrderDto;
import com.snakeporium_backend.services.customer.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
