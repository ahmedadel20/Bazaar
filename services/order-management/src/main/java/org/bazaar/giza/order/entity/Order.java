package org.bazaar.giza.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bazaar.giza.transaction.entity.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders", schema = "bazaar")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bazaar_user_id", nullable = false)
    private Long bazaarUserId;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "final_price", nullable = false)
    private BigDecimal finalPrice;
    @Column(name = "order_date", nullable = false)
    private Date orderDate;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Transaction> transactions;  // Add a field for transactions
}