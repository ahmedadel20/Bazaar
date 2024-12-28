package org.bazaar.productCatalogue.saleStatus.repo;

import org.bazaar.productCatalogue.saleStatus.entity.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleStatusRepo extends JpaRepository<SaleStatus, Integer> {

}
