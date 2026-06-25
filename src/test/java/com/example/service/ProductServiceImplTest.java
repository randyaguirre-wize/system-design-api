package com.example.service;

import com.example.dto.ProductRequest;
import com.example.exception.ProductNotFoundException;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsAllProducts() {
        Product p = new Product();
        p.setName("Widget");
        p.setPrice(new BigDecimal("9.99"));
        when(repository.findAll()).thenReturn(List.of(p));

        List<Product> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Widget");
    }

    @Test
    void findById_existingId_returnsProduct() {
        Product p = new Product();
        p.setName("Widget");
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        Product result = service.findById(1L);

        assertThat(result.getName()).isEqualTo("Widget");
    }

    @Test
    void findById_unknownId_throwsProductNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessage("Product with id 99 not found");
    }

    @Test
    void create_savesAndReturnsProduct() {
        ProductRequest req = new ProductRequest();
        req.setName("Gadget");
        req.setPrice(new BigDecimal("19.99"));

        Product saved = new Product();
        saved.setName("Gadget");
        saved.setPrice(new BigDecimal("19.99"));
        when(repository.save(any(Product.class))).thenReturn(saved);

        Product result = service.create(req);

        assertThat(result.getName()).isEqualTo("Gadget");
        assertThat(result.getPrice()).isEqualByComparingTo("19.99");
        verify(repository).save(any(Product.class));
    }

    @Test
    void update_existingId_updatesNameAndPrice() {
        Product existing = new Product();
        existing.setName("Old");
        existing.setPrice(new BigDecimal("1.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        ProductRequest req = new ProductRequest();
        req.setName("New");
        req.setPrice(new BigDecimal("2.00"));

        Product result = service.update(1L, req);

        assertThat(result.getName()).isEqualTo("New");
        assertThat(result.getPrice()).isEqualByComparingTo("2.00");
        verify(repository).save(existing);
    }

    @Test
    void update_unknownId_throwsProductNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, new ProductRequest()))
            .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void delete_existingId_deletesProduct() {
        Product p = new Product();
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_unknownId_throwsProductNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
            .isInstanceOf(ProductNotFoundException.class);
    }
}
