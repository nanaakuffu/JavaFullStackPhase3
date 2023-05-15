package com.vodafone.sportyshoes.dao;

import com.vodafone.sportyshoes.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PurchaseDao extends JpaRepository<Purchase, Long> {
    @Query(value = "select sum(p.amountPaid) from Purchase p")
    Double getRevenue();

    List<Purchase> findAllByDateCreatedBetween(LocalDateTime dateCreated, LocalDateTime dateCreated2);
//    List<Purchase> findAllByDateCreatedBetweenAndProductsProductsCategoryId(LocalDateTime dateCreated, LocalDateTime dateCreated2, Long id);
}
