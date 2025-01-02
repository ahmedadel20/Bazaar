package org.bazaar.productCatalogue.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.bazaar.productCatalogue.enums.SaleStatusEnum;
import org.bazaar.productCatalogue.saleStatus.entity.SaleStatus;
import org.bazaar.productCatalogue.saleStatus.repo.SaleStatusRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class SaleStatusRepoTest {
    @Autowired
    private SaleStatusRepo repo;

    private SaleStatus saleStatus;

    @BeforeEach
    public void setUpForEach() {
        saleStatus = new SaleStatus();
        saleStatus.setStatus(SaleStatusEnum.ACTIVE);
        repo.save(saleStatus);
        saleStatus = repo.save(saleStatus);
    }

    @AfterEach
    public void tearDownAfterEach() {
        repo.deleteAll();
    }

    @Test
    public void findByStatus_NonExistant() {
        Optional<SaleStatus> saleStatusOptional = repo.findByStatus(SaleStatusEnum.INACTIVE);
        assertTrue(saleStatusOptional.isEmpty());
    }

    @Test
    public void findByStatus_Existant() {
        Optional<SaleStatus> saleStatusOptional = repo.findByStatus(SaleStatusEnum.ACTIVE);
        assertTrue(saleStatusOptional.isPresent());
        assertEquals(saleStatus, saleStatusOptional.get());
    }
}
