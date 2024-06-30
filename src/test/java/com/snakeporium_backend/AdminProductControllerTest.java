package com.snakeporium_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snakeporium_backend.controller.admin.AdminProductController;
import com.snakeporium_backend.dto.FAQDto;
import com.snakeporium_backend.dto.ProductDto;
import com.snakeporium_backend.services.admin.faq.FAQService;
import com.snakeporium_backend.services.adminproduct.AdminProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AdminProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminProductService adminProductService;

    @Mock
    private FAQService faqService;

    @InjectMocks
    private AdminProductController adminProductController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(adminProductController).build();
    }

    @Test
    public void testAddProduct() throws Exception {
        ProductDto productDto = createProductDto(1L, "Product1", 100.0, "Description1");
        when(adminProductService.addProduct(any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(post("/api/admin/product")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("productDto", productDto))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product1"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.description").value("Description1"));
    }

    @Test
    public void testGetAllProduct() throws Exception {
        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(createProductDto(1L, "Product1", 100.0, "Description1"));
        productDtos.add(createProductDto(2L, "Product2", 200.0, "Description2"));

        when(adminProductService.getAllProducts()).thenReturn(productDtos);

        mockMvc.perform(get("/api/admin/products"))
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

        when(adminProductService.getAllProductsByName(anyString())).thenReturn(productDtos);

        mockMvc.perform(get("/api/admin/search/Product1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[0].price").value(100.0));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(adminProductService.deleteProduct(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/admin/product/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testPostFAQ() throws Exception {
        FAQDto faqDto = createFaqDto(1L, "Question1", "Answer1");

        when(faqService.postFAQ(anyLong(), any(FAQDto.class))).thenReturn(faqDto);

        mockMvc.perform(post("/api/admin/faq/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(faqDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("Question1"))
                .andExpect(jsonPath("$.answer").value("Answer1"));
    }

    @Test
    public void testGetProductById() throws Exception {
        ProductDto productDto = createProductDto(1L, "Product1", 100.0, "Description1");

        when(adminProductService.getProductById(anyLong())).thenReturn(productDto);

        mockMvc.perform(get("/api/admin/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product1"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.description").value("Description1"));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        when(adminProductService.getProductById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/admin/product/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductDto productDto = createProductDto(1L, "UpdatedProduct", 150.0, "UpdatedDescription");

        when(adminProductService.updateProduct(anyLong(), any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(put("/api/admin/product/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("productDto", productDto))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("UpdatedProduct"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.description").value("UpdatedDescription"));
    }

    private ProductDto createProductDto(Long id, String name, Double price, String description) {
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName(name);
        productDto.setPrice(price);
        productDto.setDescription(description);
        return productDto;
    }

    private FAQDto createFaqDto(Long id, String question, String answer) {
        FAQDto faqDto = new FAQDto();
        faqDto.setId(id);
        faqDto.setQuestion(question);
        faqDto.setAnswer(answer);
        return faqDto;
    }
}
