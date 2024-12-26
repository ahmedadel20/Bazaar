package org.bazaar.giza.order.service;

import org.bazaar.giza.order.dto.OrderRequest;
import org.bazaar.giza.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest orderRequest);

    String delete(Long orderId);

    OrderResponse getById(Long orderId);

    List<OrderResponse> getAllByBazaarUserId(Long bazaarUserId);
}
