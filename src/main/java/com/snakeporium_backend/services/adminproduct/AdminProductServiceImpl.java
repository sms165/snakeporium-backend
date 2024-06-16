package com.snakeporium_backend.services.adminproduct;


import com.snakeporium_backend.dto.ProductDto;
import com.snakeporium_backend.entity.Category;
import com.snakeporium_backend.entity.Product;
import com.snakeporium_backend.entity.Sex;
import com.snakeporium_backend.repository.CategoryRepository;
import com.snakeporium_backend.repository.ProductRepository;
import com.snakeporium_backend.repository.SexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;
    private final SexRepository sexRepository;

    public ProductDto addProduct(ProductDto productDto) throws IOException {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImg(productDto.getImg().getBytes());
        product.setQuantity(productDto.getQuantity());
//        product.setSpecies(productDto.getSpecies());

        product.setImageFormat(productDto.getImageFormat());
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow();
Sex sex = sexRepository.findById(productDto.getSexId()).orElseThrow();
product.setSex(sex);
        product.setCategory(category);


        return productRepository.save(product).getDto();
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    public List<ProductDto> getAllProductsByName(String name) {
        List<Product> products = productRepository.findAllByNameContaining(name);
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public ProductDto getProductById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get().getDto();
        }else{
            return null;
        }
    }

    public ProductDto updateProduct(Long productId, ProductDto productDto) throws IOException {
        // Retrieve the existing product entity from the database
        Optional<Product> optionalProduct = productRepository.findById(productId);

        // Check if the product exists
        if (optionalProduct.isPresent()) {
            // Get the existing product entity
            Product existingProduct = optionalProduct.get();

            // Update the fields of the existing product with values from the DTO
            // Use the existing value if the DTO value is null
            String name = productDto.getName() != null ? productDto.getName() : existingProduct.getName();
            String description = productDto.getDescription() != null ? productDto.getDescription() : existingProduct.getDescription();
            Double price = productDto.getPrice() != null ? productDto.getPrice() : existingProduct.getPrice();
            Integer quantity = productDto.getQuantity() != null ? productDto.getQuantity() : existingProduct.getQuantity();
//            String species = productDto.getSpecies() != null ? productDto.getSpecies() : existingProduct.getSpecies();
//            String sex = productDto.getSex() != null ? productDto.getSex() : existingProduct.getSex();
            String imageFormat = productDto.getImageFormat() != null ? productDto.getImageFormat() : existingProduct.getImageFormat();
            String latin = productDto.getLatin() != null ? productDto.getLatin() : existingProduct.getLatin();

            // Update the existing product with the new or existing values
            existingProduct.setName(name);
            existingProduct.setDescription(description);
            existingProduct.setPrice(price);
            existingProduct.setQuantity(quantity);
//            existingProduct.setSpecies(species);
//            existingProduct.setSex(sex);
            existingProduct.setLatin(latin);
            existingProduct.setImageFormat(imageFormat);

            // Update category if a new category ID is provided, and it exists in the database
            if (productDto.getCategoryId() != null && !productDto.getCategoryId().equals(existingProduct.getCategory().getId())) {
                Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
                if (optionalCategory.isPresent()) {
                    existingProduct.setCategory(optionalCategory.get());
                } else {
                    throw new IllegalArgumentException("Category not found for ID: " + productDto.getCategoryId());
                }
            }

            if (productDto.getSexId() != null && !productDto.getSexId().equals(existingProduct.getSex().getId())) {
                Optional<Sex> optionalSex = sexRepository.findById(productDto.getSexId());
                if (optionalSex.isPresent()) {
                    existingProduct.setSex(optionalSex.get());
                } else {
                    throw new IllegalArgumentException("Sex not found for ID: " + productDto.getCategoryId());
                }
            }

            // If image is provided, update it
            if (productDto.getImg() != null) {
                existingProduct.setImg(productDto.getImg().getBytes());
            }

            // Validate required fields
            if (name == null || price == null || quantity == null) {
                throw new IllegalArgumentException("Name, price, and quantity are required fields.");
            }

            // Save the updated product entity
            Product updatedProduct = productRepository.save(existingProduct);

            // Convert the updated product entity to DTO and return
            return updatedProduct.getDto();
        } else {
            throw new IllegalArgumentException("Product not found for ID: " + productId);
        }
    }

}
