package com.vodafone.sportyshoes.dao;

import com.vodafone.sportyshoes.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryDao extends JpaRepository<ProductCategory, Long> {
    int countByDeletedFalse();

    List<ProductCategory> findAllByDeletedFalse();
}
