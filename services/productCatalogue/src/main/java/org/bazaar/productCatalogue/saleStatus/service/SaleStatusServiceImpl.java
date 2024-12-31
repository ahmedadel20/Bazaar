package org.bazaar.productCatalogue.saleStatus.service;

import java.util.List;
import java.util.Optional;

import org.bazaar.productCatalogue.constant.ErrorMessage;
import org.bazaar.productCatalogue.enums.SaleStatusEnum;
import org.bazaar.productCatalogue.saleStatus.entity.SaleStatus;
import org.bazaar.productCatalogue.saleStatus.exception.SaleStatusException;
import org.bazaar.productCatalogue.saleStatus.repo.SaleStatusRepo;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SaleStatusServiceImpl implements SaleStatusService {
    private final SaleStatusRepo repo;

    @Override
    public SaleStatus getSaleStatusFromId(int id) {
        Optional<SaleStatus> saleStatusOptional = repo.findById(id);
        if (saleStatusOptional.isEmpty()) {
            throw new SaleStatusException(ErrorMessage.SALE_STATUS_ID_NOT_FOUND);
        }

        return saleStatusOptional.get();
    }

    @Override
    public SaleStatus getSaleStatusFromStatus(SaleStatusEnum status) {
        Optional<SaleStatus> saleStatusOptional = repo.findByStatus(status);
        if (saleStatusOptional.isEmpty()) {
            throw new SaleStatusException(ErrorMessage.SALE_STATUS_STATUS_NOT_FOUND);
        }

        return saleStatusOptional.get();
    }

    @Override
    public List<SaleStatus> getAllStatuses() {
        return repo.findAll();
    }

}
