package com.vodafone.sportyshoes.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "cart_products")
@EntityListeners(AuditingEntityListener.class)
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    private int quantity;
    @CreatedDate
    private LocalDateTime dateCreated;
    @LastModifiedDate
    private LocalDateTime dateUpdated;

    @ManyToOne(fetch = FetchType.EAGER)
    private Purchase purchase;
}
