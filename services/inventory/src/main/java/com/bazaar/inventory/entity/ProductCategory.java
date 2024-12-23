package com.bazaar.inventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="product_categories")
public class ProductCategory {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;
}
