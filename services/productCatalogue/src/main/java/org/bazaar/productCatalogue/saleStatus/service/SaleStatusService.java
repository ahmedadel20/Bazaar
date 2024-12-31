package org.bazaar.productCatalogue.saleStatus.service;

import java.util.List;

import org.bazaar.productCatalogue.enums.SaleStatusEnum;
import org.bazaar.productCatalogue.saleStatus.entity.SaleStatus;

public interface SaleStatusService {
    SaleStatus getSaleStatusFromId(int id);

    SaleStatus getSaleStatusFromStatus(SaleStatusEnum status);

    List<SaleStatus> getAllStatuses();
}
