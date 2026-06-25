package com.example.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class ProductEntityTest {

    @Test
    void prePersist_setsCreatedAt() {
        Product product = new Product();
        product.setName("Widget");
        product.setPrice(new BigDecimal("9.99"));

        product.onCreate(); // call lifecycle method directly

        assertThat(product.getCreatedAt()).isNotNull();
    }

    @Test
    void prePersist_doesNotOverwriteExistingCreatedAt() {
        Product product = new Product();
        product.onCreate();
        var first = product.getCreatedAt();

        product.onCreate();

        assertThat(product.getCreatedAt()).isEqualTo(first);
    }
}
