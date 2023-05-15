package com.vodafone.sportyshoes.entities;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "product_categories")
@Builder
@NoArgsConstructor
@SQLDelete(sql = "UPDATE product_categories SET deleted = true WHERE id=?")
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private boolean deleted;
    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<Product> products;
    @CreatedDate
    private LocalDateTime dateCreated;
    @LastModifiedDate
    private LocalDateTime dateUpdated;
}
