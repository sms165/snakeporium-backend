package com.snakeporium_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snakeporium_backend.controller.customer.CartController;
import com.snakeporium_backend.dto.AddProductInCartDto;
import com.snakeporium_backend.dto.OrderDto;
import com.snakeporium_backend.dto.PlaceOrderDto;
import com.snakeporium_backend.exceptions.ValidationException;
import com.snakeporium_backend.services.customer.cart.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    public void testAddProductToCart() throws Exception {
        AddProductInCartDto addProductInCartDto = new AddProductInCartDto();
        // Add necessary fields to addProductInCartDto

        when(cartService.addProductToCart(any(AddProductInCartDto.class))).thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        mockMvc.perform(post("/api/customer/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addProductInCartDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddCartByUserId() throws Exception {
        Long userId = 2L;
        OrderDto orderDto = new OrderDto();
        // Add necessary fields to orderDto

        when(cartService.getCardByUserId(anyLong())).thenReturn(orderDto);

        mockMvc.perform(get("/api/customer/cart/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist()); // Modify according to OrderDto fields
    }

    @Test
    public void testRemoveItemFromCart() throws Exception {
        Long userId = 2L;
        Long productId = 2L;

        when(cartService.removeItemFromCart(anyLong(), anyLong())).thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        mockMvc.perform(delete("/api/customer/cart/{userId}/{productId}", userId, productId))
                .andExpect(status().isOk());
    }

    @Test
    public void testApplyCoupon() throws Exception {
        Long userId = 2L;
        String code = "DISCOUNT10";
        OrderDto orderDto = new OrderDto();
        // Add necessary fields to orderDto

        when(cartService.applyCoupon(anyLong(), anyString())).thenReturn(orderDto);

        mockMvc.perform(get("/api/customer/coupon/{userId}/{code}", userId, code))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist()); // Modify according to OrderDto fields
    }

    @Test
    public void testIncreaseProductQuantity() throws Exception {
        AddProductInCartDto addProductInCartDto = new AddProductInCartDto();
        // Add necessary fields to addProductInCartDto
        OrderDto orderDto = new OrderDto();
        // Add necessary fields to orderDto

        when(cartService.increaseProductQuantity(any(AddProductInCartDto.class))).thenReturn(orderDto);

        mockMvc.perform(post("/api/customer/addition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addProductInCartDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist()); // Modify according to OrderDto fields
    }

    @Test
    public void testDecreaseProductQuantity() throws Exception {
        AddProductInCartDto addProductInCartDto = new AddProductInCartDto();
        // Add necessary fields to addProductInCartDto
        OrderDto orderDto = new OrderDto();
        // Add necessary fields to orderDto

        when(cartService.decreaseProductQuantity(any(AddProductInCartDto.class))).thenReturn(orderDto);

        mockMvc.perform(post("/api/customer/deduction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addProductInCartDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist()); // Modify according to OrderDto fields
    }

    @Test
    public void testPlaceOrder() throws Exception {
        PlaceOrderDto placeOrderDto = new PlaceOrderDto();
        // Add necessary fields to placeOrderDto
        OrderDto orderDto = new OrderDto();
        // Add necessary fields to orderDto

        when(cartService.placeOrder(any(PlaceOrderDto.class))).thenReturn(orderDto);

        mockMvc.perform(post("/api/customer/placeOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(placeOrderDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist()); // Modify according to OrderDto fields
    }

    @Test
    public void testGetMyPlacedOrders() throws Exception {
        Long userId = 2L;
        List<OrderDto> orderDtoList = new ArrayList<>();
        // Add necessary fields to orderDtoList

        when(cartService.getMyPlacedOrders(anyLong())).thenReturn(orderDtoList);

        mockMvc.perform(get("/api/customer/orders/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
}
