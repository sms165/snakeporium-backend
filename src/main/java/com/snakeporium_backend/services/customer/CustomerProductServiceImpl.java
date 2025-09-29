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
import java.util.stream.Collectors;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService {

    private final ProductRepository productRepository;

    private final FAQRepository faqRepository;

    private final ReviewRepository reviewRepository;

    // Replace with your actual Project ID
    private static final String projectId = "elite-height-428017-k9";
    // Replace with the region where your model is deployed
    private static final String location = "europe-west3";
    // Replace with the name of your Gemini model
    private static final String modelName = "gemini-1.5-flash-001";

   public List<ProductDto> getAllProducts() {
    List<Product> products = productRepository.findAllWithCategory();
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

    // Method to get predefined questions and fetch responses from OpenAI
    public List<String> getPredefinedQuestionsAndResponses(Long productId) throws IOException {
        List<String> predefinedQuestions = getPredefinedQuestionsWithProductDetails(productId);
        List<String> responses = new ArrayList<>();

        for (String question : predefinedQuestions) {
            String response = textInput(question);
            responses.add(response);
        }

        return responses;
    }

    // Additional method to generate predefined questions related to product
    public List<String> getPredefinedQuestionsWithProductDetails(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        List<String> predefinedQuestions = new ArrayList<>();
        if (product.isPresent()) {
            ProductDto productDto = product.get().getDto(); // Fetch the ProductDto

            predefinedQuestions.add(String.format("What are the key features of %s, %s?", productDto.getName(),
                    productDto.getLatin()));
            predefinedQuestions.add(String.format("What do I need to keep %s, %s?", productDto.getName(),
                    productDto.getLatin()));
            predefinedQuestions.add(String.format("What feed do I need to buy for %s, %s?", productDto.getName(), productDto.getLatin()));
            // Add more predefined questions as needed

            return predefinedQuestions;
        } else {
            predefinedQuestions.add("Product not found");
            return predefinedQuestions;
        }

    }

    // Helper method to call Vertex AI and get response from Gemini model
    private static String textInput(String textPrompt) throws IOException {
        // Create Vertex AI client object
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {


            GenerativeModel model = new GenerativeModel(modelName, vertexAI);

            GenerateContentResponse response = model.generateContent(textPrompt);
            String output = ResponseHandler.getText(response);
            return output;
        }
    }


}
