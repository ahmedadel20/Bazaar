package org.bazaar.productCatalogue.sale.repo;

import org.bazaar.productCatalogue.sale.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepo extends JpaRepository<Sale, Long> {

}
