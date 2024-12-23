package com.bazaar.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="products")
public class Product {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="category_id")
    private ProductCategory productCategory;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private Double price;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="updated_at")
    private Timestamp updatedAt;

    @Override
    public String toString() {
        return "{%d, %s, %d, %.2f, %d, %s}"
                .formatted(
                        id,
                        name,
                        productCategory.getId(),
                        price,
                        quantity,
                        new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(updatedAt)
                );
    }
}
