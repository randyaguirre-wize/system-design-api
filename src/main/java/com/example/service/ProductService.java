package com.example.service;

import com.example.dto.ProductRequest;
import com.example.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product create(ProductRequest request);
    Product update(Long id, ProductRequest request);
    void delete(Long id);
}
