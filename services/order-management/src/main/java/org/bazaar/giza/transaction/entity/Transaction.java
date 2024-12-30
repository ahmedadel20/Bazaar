package org.bazaar.giza.transaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bazaar.giza.order.entity.Order;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transactions", schema = "bazaar")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)  // This will be the foreign key
    private Order order;  // Link to Order entity

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "final_price", nullable = false)
    private BigDecimal finalPrice;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}