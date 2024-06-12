package com.snakeporium_backend.services.customer.review;


import com.snakeporium_backend.dto.OrderProductsResponseDto;
import com.snakeporium_backend.dto.ProductDto;
import com.snakeporium_backend.dto.ReviewDto;
import com.snakeporium_backend.entity.*;
import com.snakeporium_backend.repository.OrderRepository;
import com.snakeporium_backend.repository.ProductRepository;
import com.snakeporium_backend.repository.ReviewRepository;
import com.snakeporium_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;


    public OrderProductsResponseDto getOrderProductsDetailByOrderId(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        OrderProductsResponseDto orderProductsResponseDto = new OrderProductsResponseDto();
        if (optionalOrder.isPresent()) {
            orderProductsResponseDto.setOrderAmount(optionalOrder.get().getAmount());

            List<ProductDto> productDtoList = new ArrayList<>();

            for(CartItems cartItems : optionalOrder.get().getCartItems()) {
                ProductDto productDto = new ProductDto();
                productDto.setId(cartItems.getProduct().getId());
                productDto.setName(cartItems.getProduct().getName());
                productDto.setPrice(cartItems.getProduct().getPrice());
                productDto.setQuantity(cartItems.getQuantity());

                productDto.setByteImg(cartItems.getProduct().getImg());

                productDtoList.add(productDto);

            }

            orderProductsResponseDto.setProductDtoList(productDtoList);
        }
        return orderProductsResponseDto;


    }

    public ReviewDto giveReview(ReviewDto reviewDto) throws IOException {
        // Find the product by ID
        Optional<Product> optionalProduct = productRepository.findById(reviewDto.getProductId());

        // Find the user by ID
        Optional<User> optionalUser = userRepository.findById(reviewDto.getUserId());

        // Log the optionalProduct and optionalUser
        System.out.println("Optional Product: " + optionalProduct);
        System.out.println("Optional User: " + optionalUser);

        if (optionalProduct.isPresent() && optionalUser.isPresent()) {
            // Create a new review instance
            Review review = new Review();

            // Set review properties
            review.setRating(reviewDto.getRating());
            review.setDescription(reviewDto.getDescription());
            review.setUser(optionalUser.get());
            review.setProduct(optionalProduct.get());

            // Set the image if provided
            if (reviewDto.getImg() != null) {
                review.setImg(reviewDto.getImg().getBytes());
            }

            // Save the review and return the DTO
            return reviewRepository.save(review).getDto();
        } else {
            // Handle the case where either the product or user is not found
            return null;
        }
    }
}
