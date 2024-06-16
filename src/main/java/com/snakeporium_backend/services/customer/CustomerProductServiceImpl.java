package com.snakeporium_backend.services.customer;


import com.snakeporium_backend.dto.ProductDetailDto;
import com.snakeporium_backend.dto.ProductDto;
import com.snakeporium_backend.entity.FAQ;
import com.snakeporium_backend.entity.Product;
import com.snakeporium_backend.entity.Review;
import com.snakeporium_backend.repository.FAQRepository;
import com.snakeporium_backend.repository.ProductRepository;
import com.snakeporium_backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService {

    private final ProductRepository productRepository;

    private final FAQRepository faqRepository;

    private final ReviewRepository reviewRepository;

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    public List<ProductDto> searchProductByTitle (String name) {
        List<Product> products = productRepository.findAllByNameContaining(name);
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    public ProductDetailDto getProductDetailById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if(product.isPresent()) {
            List<FAQ> faqList = faqRepository.findAllByProductId(productId);
            List<Review> reviewList = reviewRepository.findAllByProductId(productId);

            ProductDetailDto productDetailDto = new ProductDetailDto();

            productDetailDto.setProductDto(product.get().getDto());
            productDetailDto.setFaqDtoList(faqList.stream().map(FAQ ::getFAQDto).collect(Collectors.toList()));
            productDetailDto.setReviewDtoList(reviewList.stream().map(Review ::getDto).collect(Collectors.toList()));

            return productDetailDto;
        }
        return null;
    }

    public List<String> getRandomQuestionsWithProductDetails(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        List<String> randomQuestions = new ArrayList<>();
        if (product.isPresent()) {
            ProductDto productDto = product.get().getDto(); // Fetch the ProductDto

            randomQuestions.add(String.format("What are the benefits of using %s?", productDto.getName()));
            randomQuestions.add(String.format("How do I use %s?", productDto.getName()));
            randomQuestions.add(String.format("Can %s be customized?", productDto.getName()));
            // Add more questions as needed

            return randomQuestions;
        } else {
            randomQuestions.add("Product not found");
            return randomQuestions;
        }
    }

}
