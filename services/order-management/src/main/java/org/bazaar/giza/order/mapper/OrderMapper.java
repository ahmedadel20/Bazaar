package org.bazaar.giza.order.mapper;

import org.bazaar.giza.order.dto.OrderRequest;
import org.bazaar.giza.order.dto.OrderResponse;
import org.bazaar.giza.order.entity.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {
    public OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .bazaarUserId(order.getBazaarUserId())
                .description(order.getDescription())
                .orderDate(order.getOrderDate())
                .build();
    }

    public Order toOrder(OrderRequest orderRequest) {
        return Order.builder()
                .id(orderRequest.id())
                .bazaarUserId(orderRequest.bazaarUserId())
                .description(orderRequest.description())
                .orderDate(orderRequest.orderDate())
                .build();
    }
}
