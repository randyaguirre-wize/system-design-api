package com.example.service;

import com.example.dto.ProductRequest;
import com.example.exception.ProductNotFoundException;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product create(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return repository.save(product);
    }

    @Override
    public Product update(Long id, ProductRequest request) {
        Product product = findById(id);
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return repository.save(product);
    }

    @Override
    public void delete(Long id) {
        findById(id); // throws if not found
        repository.deleteById(id);
    }
}
