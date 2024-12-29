package org.bazaar.productCatalogue.saleStatus.repo;

import org.bazaar.productCatalogue.saleStatus.entity.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.bazaar.productCatalogue.enums.SaleStatusEnum;

public interface SaleStatusRepo extends JpaRepository<SaleStatus, Integer> {
    Optional<SaleStatus> findByStatus(SaleStatusEnum status);
}
