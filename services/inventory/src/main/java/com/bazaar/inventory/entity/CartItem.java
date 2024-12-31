package com.bazaar.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bazaar_user_id", nullable = false)
    private Long bazaarUserId;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product cartProduct;

    @Column(name="quantity", nullable = false)
    private Integer quantity;

}
