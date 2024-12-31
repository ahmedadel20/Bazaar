package org.bazaar.productCatalogue.sale.repo;

import org.bazaar.productCatalogue.sale.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.sql.Date;

public interface SaleRepo extends JpaRepository<Sale, Long> {
    // Inclusive search
    // List<Sale> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(Date
    // startDate);

    List<Sale> findByStartDate(Date startDate);

    List<Sale> findByEndDate(Date endDate);
}
