package org.bazaar.productCatalogue.sale.dto;

import java.sql.Timestamp;

// FIXME: Update to conform to the return from Inventory Service
public record ProductResponse(Long id,
                String category,
                String name,
                Double price,
                Long quantity,
                Timestamp lastUpdated) {

}
