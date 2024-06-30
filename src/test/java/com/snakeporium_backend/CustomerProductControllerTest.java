package com.snakeporium_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snakeporium_backend.controller.customer.CustomerProductController;
import com.snakeporium_backend.dto.FAQDto;
import com.snakeporium_backend.dto.ProductDetailDto;
import com.snakeporium_backend.dto.ProductDto;
import com.snakeporium_backend.dto.ReviewDto;
import com.snakeporium_backend.services.customer.CustomerProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CustomerProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerProductService customerProductService;

    @InjectMocks
    private CustomerProductController customerProductController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(customerProductController).build();
    }

    @Test
    public void testGetAllProduct() throws Exception {
        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(createProductDto(1L, "Product1", 100.0, "Description1"));
        productDtos.add(createProductDto(2L, "Product2", 200.0, "Description2"));

        when(customerProductService.getAllProducts()).thenReturn(productDtos);

        mockMvc.perform(get("/api/customer/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product2"))
                .andExpect(jsonPath("$[1].price").value(200.0));
    }

    @Test
    public void testGetAllProductByName() throws Exception {
        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(createProductDto(1L, "Product1", 100.0, "Description1"));

        when(customerProductService.searchProductByTitle(anyString())).thenReturn(productDtos);

        mockMvc.perform(get("/api/customer/search/Product1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[0].price").value(100.0));
    }

    @Test
    public void testGetAllProductDetailById() throws Exception {
        ProductDto productDto = createProductDto(1L, "Product1", 100.0, "Description1");
        List<ReviewDto> reviewDtos = new ArrayList<>();
        reviewDtos.add(createReviewDto(1L, "Review1", 5));
        List<FAQDto> faqDtos = new ArrayList<>();
        faqDtos.add(createFaqDto(1L, "FAQ1", "Answer1"));

        ProductDetailDto productDetailDto = createProductDetailDto(productDto, reviewDtos, faqDtos);

        when(customerProductService.getProductDetailById(anyLong())).thenReturn(productDetailDto);

        mockMvc.perform(get("/api/customer/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productDto.id").value(1))
                .andExpect(jsonPath("$.productDto.name").value("Product1"))
                .andExpect(jsonPath("$.productDto.price").value(100.0))
                .andExpect(jsonPath("$.productDto.description").value("Description1"))
                .andExpect(jsonPath("$.reviewDtoList[0].id").value(1))
                .andExpect(jsonPath("$.reviewDtoList[0].description").value("Review1"))
                .andExpect(jsonPath("$.reviewDtoList[0].rating").value(5))
                .andExpect(jsonPath("$.faqDtoList[0].id").value(1))
                .andExpect(jsonPath("$.faqDtoList[0].question").value("FAQ1"))
                .andExpect(jsonPath("$.faqDtoList[0].answer").value("Answer1"));
    }

    @Test
    public void testGetAllProductDetailById_NotFound() throws Exception {
        when(customerProductService.getProductDetailById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/customer/product/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetRandomQuestionsWithProductDetails() throws Exception {
        List<String> randomQuestions = new ArrayList<>();
        randomQuestions.add("What is the product made of?");
        randomQuestions.add("How long is the warranty period?");

        when(customerProductService.getRandomQuestionsWithProductDetails(anyLong())).thenReturn(randomQuestions);

        mockMvc.perform(get("/api/customer/random-questions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("What is the product made of?"))
                .andExpect(jsonPath("$[1]").value("How long is the warranty period?"));
    }

    private ProductDto createProductDto(Long id, String name, Double price, String description) {
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
productDto.setName(name);
        productDto.setPrice(price);
        productDto.setDescription(description);
        return productDto;
    }

    private ReviewDto createReviewDto(Long id, String comment, long rating) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(id);
        reviewDto.setDescription(comment);
        reviewDto.setRating(rating);
        return reviewDto;
    }

    private FAQDto createFaqDto(Long id, String question, String answer) {
        FAQDto faqDto = new FAQDto();
        faqDto.setId(id);
        faqDto.setQuestion(question);
        faqDto.setAnswer(answer);
        return faqDto;
    }

    private ProductDetailDto createProductDetailDto(ProductDto productDto, List<ReviewDto> reviewDtoList, List<FAQDto> faqDtoList) {
        ProductDetailDto productDetailDto = new ProductDetailDto();
        productDetailDto.setProductDto(productDto);
        productDetailDto.setReviewDtoList(reviewDtoList);
        productDetailDto.setFaqDtoList(faqDtoList);
        return productDetailDto;
    }
}
