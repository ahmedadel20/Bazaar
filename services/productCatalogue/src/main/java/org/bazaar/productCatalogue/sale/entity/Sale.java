package org.bazaar.productCatalogue.sale.entity;

import java.sql.Date;
import java.util.List;

import org.bazaar.productCatalogue.saleStatus.entity.SaleStatus;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private float discountPercentage;
    private Date startDate;
    private Date endDate;
    @ManyToOne
    @JoinColumn(nullable = false)
    private SaleStatus status;

    @ElementCollection
    @CollectionTable(name = "sale_products", joinColumns = @JoinColumn(name = "sale_id"))
    @Column(name = "product_id")
    private List<Long> productIds;

    @ElementCollection
    @CollectionTable(name = "sale_categories", joinColumns = @JoinColumn(name = "sale_id"))
    @Column(name = "category_id")
    private List<Long> categoryIds;
}
