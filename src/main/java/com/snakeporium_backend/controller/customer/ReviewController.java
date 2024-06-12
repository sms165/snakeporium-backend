package com.snakeporium_backend.controller.customer;

import com.snakeporium_backend.dto.OrderProductsResponseDto;
import com.snakeporium_backend.dto.ReviewDto;
import com.snakeporium_backend.services.customer.review.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class ReviewController {

    private final ReviewService reviewService;



    @GetMapping("/ordered-products/{orderId}")
    public ResponseEntity<OrderProductsResponseDto> getOrderProductsDetailsByOrderId(@PathVariable Long orderId) {

        return ResponseEntity.ok(reviewService.getOrderProductsDetailByOrderId(orderId));
    }

    @PostMapping("/review")
    public ResponseEntity<?> giveReview(@ModelAttribute ReviewDto reviewDto) throws IOException {

        System.out.println("Received ReviewDto: " + reviewDto);
        ReviewDto review = reviewService.giveReview(reviewDto);
        if (review == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }
}
