package com.vodafone.sportyshoes.dao;

import com.vodafone.sportyshoes.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Long> {
    List<Product> findAllByDeletedFalseAndCategoryDeletedFalse();

//    int countByDeletedFalse();

    int countByDeletedFalseAndCategoryDeletedFalse();
}
