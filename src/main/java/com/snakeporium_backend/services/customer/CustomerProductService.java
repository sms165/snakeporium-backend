package com.snakeporium_backend.services.customer;

import com.snakeporium_backend.dto.ProductDetailDto;
import com.snakeporium_backend.dto.ProductDto;

import java.util.List;

public interface CustomerProductService {

    List<ProductDto> getAllProducts();

    List<ProductDto> searchProductByTitle(String name);

    ProductDetailDto getProductDetailById(Long productId);
}
