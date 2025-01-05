package org.bazaar.productCatalogue.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bazaar.productCatalogue.enums.SaleStatusEnum;
import org.bazaar.productCatalogue.sale.entity.Sale;
import org.bazaar.productCatalogue.sale.repo.SaleRepo;
import org.bazaar.productCatalogue.saleStatus.entity.SaleStatus;
import org.bazaar.productCatalogue.saleStatus.repo.SaleStatusRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class SaleRepoTest {
    @Autowired
    private SaleRepo saleRepo;

    @Autowired
    private SaleStatusRepo saleStatusRepo;

    private static Date startDate;
    private static Date endDate;
    private static SaleStatus saleStatus;
    private static List<Long> categoryIds;
    // Includes category ids not in categoryIds
    private static List<Long> unsavedCategoryIds;
    // Includes category ids that are in categoryIds but not all
    private static List<Long> savedCategoryIds;

    private Sale sale;

    @BeforeAll
    public static void setUp() {
        startDate = Date.valueOf("2026-01-01");
        endDate = Date.valueOf("2026-2-01");

        saleStatus = new SaleStatus(0, SaleStatusEnum.ACTIVE);

        categoryIds = new ArrayList<>(List.of(1L, 4L, 11L));
        unsavedCategoryIds = new ArrayList<>(List.of(7L, 8L));
        savedCategoryIds = new ArrayList<>(List.of(1L, 3L));
    }

    @BeforeEach
    public void setUpForEach() {
        saleStatus = new SaleStatus();
        saleStatus.setStatus(SaleStatusEnum.ACTIVE);
        saleStatus = saleStatusRepo.save(saleStatus);

        sale = new Sale(null, "A sale", 0.5f, startDate, endDate, saleStatus, new ArrayList<>(), categoryIds);
        saleRepo.save(sale);
    }

    @AfterEach
    public void tearDownAfterEach() {
        saleRepo.deleteAll();
    }

    @Test
    public void findByName_NonExitant() {
        Optional<Sale> saleOptional = saleRepo.findByName("Blah");
        assertTrue(saleOptional.isEmpty());
    }

    @Test
    public void findByName_Exitant() {
        Optional<Sale> saleOptional = saleRepo.findByName(sale.getName());
        assertTrue(saleOptional.isPresent());
        assertEquals(sale, saleOptional.get());
    }

    @Test
    public void findByStartDate_NonExitant() {
        List<Sale> sales = saleRepo.findByStartDate(Date.valueOf("1990-01-01"));
        assertTrue(sales.isEmpty());
    }

    @Test
    public void findByStartDate_Exitant() {
        List<Sale> sales = saleRepo.findByStartDate(startDate);
        assertTrue(sales.size() == 1);
        assertTrue(sales.contains(sale));
    }

    @Test
    public void findByEndDate_NonExitant() {
        List<Sale> sales = saleRepo.findByEndDate(Date.valueOf("1990-01-01"));
        assertTrue(sales.isEmpty());
    }

    @Test
    public void findByEndDate_Exitant() {
        List<Sale> sales = saleRepo.findByEndDate(endDate);
        assertTrue(sales.size() == 1);
        assertTrue(sales.contains(sale));
    }

    @Test
    public void findByCategoryIdsAndStartDateBeforeAndEndDateAfter_NotOverlapping() {
        // Exact match category ids
        Optional<Sale> saleOptional1 = saleRepo.findByCategoryIdsAndOverlappingDates(
                categoryIds,
                Date.valueOf("2020-01-01"),
                Date.valueOf("2020-01-02"));
        assertTrue(saleOptional1.isEmpty());

        // Partial match category ids
        Optional<Sale> saleOptional2 = saleRepo.findByCategoryIdsAndOverlappingDates(
                savedCategoryIds,
                Date.valueOf("2020-01-01"),
                Date.valueOf("2020-01-02"));
        assertTrue(saleOptional2.isEmpty());

        // No match category ids
        Optional<Sale> saleOptional3 = saleRepo.findByCategoryIdsAndOverlappingDates(
                unsavedCategoryIds,
                Date.valueOf("2020-01-01"),
                Date.valueOf("2020-01-02"));
        assertTrue(saleOptional3.isEmpty());
    }

    @Test
    public void findByCategoryIdsAndStartDateBeforeAndEndDateAfter_Overlapping() {
        // Exact match category ids
        Optional<Sale> saleOptional1 = saleRepo.findByCategoryIdsAndOverlappingDates(
                categoryIds,
                Date.valueOf("2020-01-01"),
                startDate);
        assertTrue(saleOptional1.isPresent());
        assertEquals(sale, saleOptional1.get());

        // Partial match category ids
        Optional<Sale> saleOptional2 = saleRepo.findByCategoryIdsAndOverlappingDates(
                savedCategoryIds,
                Date.valueOf("2020-01-01"),
                startDate);
        assertTrue(saleOptional2.isPresent());
        assertEquals(sale, saleOptional2.get());

        // No match category ids
        Optional<Sale> saleOptional3 = saleRepo.findByCategoryIdsAndOverlappingDates(
                unsavedCategoryIds,
                Date.valueOf("2020-01-01"),
                startDate);
        assertTrue(saleOptional3.isEmpty());
    }

    @Test
    public void findByCategoryIdsAndStartDateBeforeAndEndDateAfter_ExactDates() {
        // Exact match category ids
        Optional<Sale> saleOptional1 = saleRepo.findByCategoryIdsAndOverlappingDates(
                categoryIds,
                startDate,
                endDate);
        assertTrue(saleOptional1.isPresent());
        assertEquals(sale, saleOptional1.get());

        // Partial match category ids
        Optional<Sale> saleOptional2 = saleRepo.findByCategoryIdsAndOverlappingDates(
                savedCategoryIds,
                startDate,
                endDate);
        assertTrue(saleOptional2.isPresent());
        assertEquals(sale, saleOptional2.get());

        // No match category ids
        Optional<Sale> saleOptional3 = saleRepo.findByCategoryIdsAndOverlappingDates(
                unsavedCategoryIds,
                Date.valueOf("2020-01-01"),
                endDate);
        assertTrue(saleOptional3.isEmpty());
    }

}
