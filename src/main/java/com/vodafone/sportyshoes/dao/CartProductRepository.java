package com.vodafone.sportyshoes.dao;

import com.vodafone.sportyshoes.entities.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    @Query(value = "select sum(cp.quantity) from CartProduct cp where cp.purchase is not null")
    Long getItemsSold();
}