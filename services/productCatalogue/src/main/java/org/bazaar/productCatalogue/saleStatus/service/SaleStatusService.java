package org.bazaar.productCatalogue.saleStatus.service;

import java.util.List;

public interface SaleStatusService {
    String getStatusFromId(int id);

    List<String> getAllStatuses();
}
