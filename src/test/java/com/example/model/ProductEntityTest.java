package com.example.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductEntityTest {

    @Test
    void product_fieldsAreSetCorrectly() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Widget");
        product.setPrice(new BigDecimal("9.99"));
        product.setCreatedAt(LocalDateTime.of(2024, 1, 1, 0, 0));

        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("Widget");
        assertThat(product.getPrice()).isEqualByComparingTo("9.99");
        assertThat(product.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
    }
}
