package org.bazaar.productCatalogue;

import java.util.ArrayList;

import org.bazaar.productCatalogue.enums.SaleStatusEnum;
import org.bazaar.productCatalogue.sale.entity.Sale;
import org.bazaar.productCatalogue.sale.repo.SaleRepo;
import org.bazaar.productCatalogue.saleStatus.entity.SaleStatus;
import org.bazaar.productCatalogue.saleStatus.repo.SaleStatusRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
@Transactional
public class DataInitializer implements CommandLineRunner {
    private final SaleStatusRepo saleStatusRepo;
    private final SaleRepo saleRepo;

    @Override
    public void run(String... args) throws Exception {
        SaleStatus activeSaleStatus = new SaleStatus(0, SaleStatusEnum.ACTIVE);
        SaleStatus inactiveSaleStatus = new SaleStatus(0, SaleStatusEnum.INACTIVE);

        activeSaleStatus = saleStatusRepo.save(activeSaleStatus);
        inactiveSaleStatus = saleStatusRepo.save(inactiveSaleStatus);

        Sale sale = Sale.builder()
                .name("Summer Sale")
                .startDate(null)
                .endDate(null)
                .status(inactiveSaleStatus)
                .productIds(new ArrayList<>())
                .discountPercentage(0.5f)
                .build();

        saleRepo.save(sale);
    }
}
