package com.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private BigDecimal price;
    private LocalDateTime createdAt;
}
