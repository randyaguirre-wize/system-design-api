package com.example.controller;

import com.example.dto.ProductRequest;
import com.example.exception.ProductNotFoundException;
import com.example.model.Product;
import com.example.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product sampleProduct() {
        Product p = new Product();
        p.setName("Widget");
        p.setPrice(new BigDecimal("9.99"));
        return p;
    }

    @Test
    void getAll_returns200WithList() throws Exception {
        when(productService.findAll()).thenReturn(List.of(sampleProduct()));

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Widget"));
    }

    @Test
    void getById_existingId_returns200() throws Exception {
        when(productService.findById(1L)).thenReturn(sampleProduct());

        mockMvc.perform(get("/api/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Widget"));
    }

    @Test
    void getById_unknownId_returns404() throws Exception {
        when(productService.findById(99L)).thenThrow(new ProductNotFoundException(99L));

        mockMvc.perform(get("/api/products/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void create_validBody_returns201() throws Exception {
        ProductRequest req = new ProductRequest();
        req.setName("Gadget");
        req.setPrice(new BigDecimal("19.99"));
        when(productService.create(any(ProductRequest.class))).thenReturn(sampleProduct());

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated());
    }

    @Test
    void create_blankName_returns400WithFieldError() throws Exception {
        ProductRequest req = new ProductRequest();
        req.setName("");
        req.setPrice(new BigDecimal("9.99"));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.fields.name").exists());
    }

    @Test
    void update_existingId_returns200() throws Exception {
        ProductRequest req = new ProductRequest();
        req.setName("Updated");
        req.setPrice(new BigDecimal("5.00"));
        when(productService.update(eq(1L), any(ProductRequest.class))).thenReturn(sampleProduct());

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk());
    }

    @Test
    void delete_existingId_returns204() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/products/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void delete_unknownId_returns404() throws Exception {
        doThrow(new ProductNotFoundException(99L)).when(productService).delete(99L);

        mockMvc.perform(delete("/api/products/99"))
            .andExpect(status().isNotFound());
    }
}
