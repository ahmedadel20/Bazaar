package org.bazaar.productCatalogue.saleStatus.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bazaar.productCatalogue.constant.ErrorMessage;
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
    public String getStatusFromId(int id) {
        Optional<SaleStatus> saleStatusOptional = repo.findById(id);
        if (saleStatusOptional.isEmpty()) {
            throw new SaleStatusException(ErrorMessage.SALE_STATUS_ID_NOT_FOUND);
        }

        return saleStatusOptional.get().getSaleStatus().toString();
    }

    @Override
    public List<String> getAllStatuses() {
        List<String> statuses = new ArrayList<>();

        List<SaleStatus> saleStatuses = repo.findAll();
        for (SaleStatus saleStatus : saleStatuses) {
            statuses.add(saleStatus.getSaleStatus().toString());
        }

        return statuses;
    }

}
