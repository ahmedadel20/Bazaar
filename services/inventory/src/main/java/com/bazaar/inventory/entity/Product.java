package com.bazaar.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="products")
public class Product {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category productCategory;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private Double price;

    @Column(name="quantity")
    private Long quantity;

    @Column(name="updated_at")
    private Timestamp lastUpdated;

}
