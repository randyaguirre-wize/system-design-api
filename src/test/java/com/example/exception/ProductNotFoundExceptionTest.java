package com.example.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ProductNotFoundExceptionTest {

    @Test
    void message_containsId() {
        var ex = new ProductNotFoundException(42L);
        assertThat(ex.getMessage()).isEqualTo("Product with id 42 not found");
    }
}
