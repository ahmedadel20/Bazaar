package org.bazaar.productCatalogue.sale.repo;

import org.bazaar.productCatalogue.sale.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.sql.Date;

public interface SaleRepo extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s " +
            "JOIN s.categoryIds c " +
            "WHERE c IN :categoryIds " +
            "AND s.startDate <= :endDate " +
            "AND s.endDate >= :startDate")
    Optional<Sale> findByCategoryIdsAndOverlappingDates(
            @Param("categoryIds") List<Long> categoryIds,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    Optional<Sale> findByName(String name);

    List<Sale> findByStartDate(Date startDate);

    List<Sale> findByEndDate(Date endDate);
}
