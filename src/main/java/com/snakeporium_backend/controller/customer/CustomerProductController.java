package com.snakeporium_backend.controller.customer;


import com.snakeporium_backend.dto.ProductDetailDto;
import com.snakeporium_backend.dto.ProductDto;
import com.snakeporium_backend.services.customer.CustomerProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerProductController {

    private final CustomerProductService customerProductService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        List<ProductDto> productDtos = customerProductService.getAllProducts();
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDto>> getAllProductByName(@PathVariable String name) {
        List<ProductDto> productDtos = customerProductService.searchProductByTitle(name);
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDetailDto> getAllProductDetailById(@PathVariable Long productId) {
        ProductDetailDto productDetailDto = customerProductService.getProductDetailById(productId);
        if(productDetailDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDetailDto);
    }

    @GetMapping("/random-questions/{productId}")
    public ResponseEntity<List<String>> getRandomQuestionsWithProductDetails(@PathVariable Long productId) {
        List<String> randomQuestions = customerProductService.getRandomQuestionsWithProductDetails(productId);
        return ResponseEntity.ok(randomQuestions);
    }

    // New endpoint to get predefined questions and responses from OpenAI
    @GetMapping("/details/{productId}")
    public ResponseEntity<List<String>> getPredefinedQuestionsAndResponses(@PathVariable Long productId) {
        List<String> responses = customerProductService.getPredefinedQuestionsAndResponses(productId);
        return ResponseEntity.ok(responses);
    }

}
