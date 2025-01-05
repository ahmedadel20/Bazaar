package org.bazaar.productCatalogue.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bazaar.productCatalogue.constant.ErrorMessage;
import org.bazaar.productCatalogue.enums.SaleStatusEnum;
import org.bazaar.productCatalogue.saleStatus.entity.SaleStatus;
import org.bazaar.productCatalogue.saleStatus.exception.SaleStatusException;
import org.bazaar.productCatalogue.saleStatus.repo.SaleStatusRepo;
import org.bazaar.productCatalogue.saleStatus.service.SaleStatusService;
import org.bazaar.productCatalogue.saleStatus.service.SaleStatusServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SaleStatusServiceTest {
    @TestConfiguration
    static class ServiceTestConfig {
        @Bean
        SaleStatusService service(SaleStatusRepo repo) {
            return new SaleStatusServiceImpl(repo);
        }
    }

    @MockitoBean
    private SaleStatusRepo repo;

    @Autowired
    private SaleStatusService service;

    private static SaleStatus saleStatus;
    private static List<SaleStatus> saleStatuses;

    @BeforeAll
    public static void setUp() {
        saleStatus = new SaleStatus(1, SaleStatusEnum.ACTIVE);

        saleStatuses = new ArrayList<>();
        saleStatuses.add(saleStatus);
    }

    @BeforeEach
    public void setUpForEach() {
        when(repo.findById(saleStatus.getId())).thenReturn(Optional.of(saleStatus));
        when(repo.findById(-1)).thenReturn(Optional.empty());

        when(repo.findByStatus(SaleStatusEnum.ACTIVE)).thenReturn(Optional.of(saleStatus));
        when(repo.findByStatus(SaleStatusEnum.INACTIVE)).thenReturn(Optional.empty());

        when(repo.findAll()).thenReturn(saleStatuses);
    }

    @Test
    public void getSaleStatusFromId_NonExistant() {
        SaleStatusException ex = assertThrows(SaleStatusException.class,
                () -> {
                    service.getSaleStatusFromId(-1);
                });
        assertEquals(ErrorMessage.SALE_STATUS_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    public void getSaleStatusFromId_Existant() {
        assertEquals(saleStatus, service.getSaleStatusFromId(saleStatus.getId()));
    }

    @Test
    public void getSaleStatusFromStatus_NonExistant() {
        SaleStatusException ex = assertThrows(SaleStatusException.class,
                () -> {
                    service.getSaleStatusFromStatus(SaleStatusEnum.INACTIVE);
                });
        assertEquals(ErrorMessage.SALE_STATUS_STATUS_NOT_FOUND, ex.getMessage());
    }

    @Test
    public void getSaleStatusFromStatus_Existant() {
        assertEquals(saleStatus, service.getSaleStatusFromStatus(SaleStatusEnum.ACTIVE));
    }

    @Test
    public void getAllStatuses() {
        assertEquals(saleStatuses, service.getAllStatuses());
    }
}
