package org.bazaar.giza.order.service;

import org.bazaar.giza.order.entity.Order;

import java.util.List;

public interface OrderService {
    Order create(Order order);

    String delete(Long orderId);

    Order getById(Long orderId);

    List<Order> getAllByBazaarUserId(Long bazaarUserId);
}
