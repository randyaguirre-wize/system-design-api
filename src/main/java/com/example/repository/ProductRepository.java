package com.example.repository;

import com.example.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final NamedParameterJdbcTemplate jdbc;

    private static final RowMapper<Product> ROW_MAPPER = (rs, rowNum) -> {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return p;
    };

    public List<Product> findAll() {
        return jdbc.query("SELECT id, name, price, created_at FROM products", ROW_MAPPER);
    }

    public Optional<Product> findById(Long id) {
        List<Product> results = jdbc.query(
            "SELECT id, name, price, created_at FROM products WHERE id = :id",
            Map.of("id", id),
            ROW_MAPPER
        );
        return results.stream().findFirst();
    }

    public Product save(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
            "INSERT INTO products (name, price, created_at) VALUES (:name, :price, :createdAt)",
            new MapSqlParameterSource()
                .addValue("name", product.getName())
                .addValue("price", product.getPrice())
                .addValue("createdAt", product.getCreatedAt()),
            keyHolder,
            new String[]{"id"}
        );
        product.setId(keyHolder.getKey().longValue());
        return product;
    }

    public Product update(Product product) {
        jdbc.update(
            "UPDATE products SET name = :name, price = :price WHERE id = :id",
            new MapSqlParameterSource()
                .addValue("name", product.getName())
                .addValue("price", product.getPrice())
                .addValue("id", product.getId())
        );
        return product;
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM products WHERE id = :id", Map.of("id", id));
    }
}
