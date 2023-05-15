package com.vodafone.sportyshoes.dtos;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private long price;
    private int size;
    private String imageUrl;
    private String category;
}
