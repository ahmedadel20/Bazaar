package org.bazaar.giza.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "order", schema = "bazaar")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "bazaar_user_id", nullable = false)
    Long bazaarUserId;
    @Column(name = "description", nullable = false)
    String description;
    @Column(name = "order_date", nullable = false)
    Date orderDate;
}
