package com.example.catalog;

import com.example.catalog.dto.ProductRequest;
import com.example.catalog.model.Producer;
import com.example.catalog.repository.ProducerRepository;
import com.example.catalog.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Rollback po każdym teście
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ProductRepository productRepository;

    private Long testProducerId;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        producerRepository.deleteAll();

        Producer producer = producerRepository.save(Producer.builder().name("Test Producer").build());
        testProducerId = producer.getId();
    }

    @Test
    void shouldCreateProductWithAttributes() throws Exception {
        ProductRequest request = ProductRequest.builder()
                .producerId(testProducerId)
                .name("Smartphone X")
                .description("Flagship smartphone")
                .price(new BigDecimal("999.99"))
                .attributes(Map.of(
                        "color", "Black",
                        "memory", "256GB"
                ))
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Smartphone X")))
                .andExpect(jsonPath("$.producer.name", is("Test Producer")))
                .andExpect(jsonPath("$.attributes.color", is("Black")))
                .andExpect(jsonPath("$.attributes.memory", is("256GB")));
    }

    @Test
    void shouldReturnValidationErrorsForInvalidRequest() throws Exception {
        ProductRequest invalidRequest = ProductRequest.builder()
                .name("") // Blank name
                .price(new BigDecimal("-10")) // Negative price
                .build(); // Missing producerId

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
