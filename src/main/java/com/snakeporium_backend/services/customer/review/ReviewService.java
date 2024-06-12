package com.snakeporium_backend.services.customer.review;

import com.snakeporium_backend.dto.OrderProductsResponseDto;
import com.snakeporium_backend.dto.ReviewDto;

import java.io.IOException;

public interface ReviewService {

    OrderProductsResponseDto getOrderProductsDetailByOrderId(Long orderId);

    ReviewDto giveReview(ReviewDto reviewDto) throws IOException;
}
