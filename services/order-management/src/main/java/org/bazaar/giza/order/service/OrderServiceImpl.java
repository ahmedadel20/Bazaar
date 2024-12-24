package org.bazaar.giza.order.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.order.dto.OrderRequest;
import org.bazaar.giza.order.dto.OrderResponse;
import org.bazaar.giza.order.entity.Order;
import org.bazaar.giza.order.mapper.OrderMapper;
import org.bazaar.giza.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public OrderResponse create(OrderRequest orderRequest) {
        Order order = repository.save(mapper.toOrder(orderRequest));
        return mapper.toOrderResponse(order);
    }

    @Override
    public String delete(Long orderId) {
        repository.deleteById(orderId);
        return "Order deleted";
    }

    @Override
    public OrderResponse getById(Long orderId) {
        return repository.findById(orderId).map(mapper::toOrderResponse).orElseThrow(); //add custom exception
    }

    @Override
    public List<OrderResponse> getAllByBazaarUserId(Long bazaarUserId) {
        return repository.findAllByBazaarUserId(bazaarUserId).stream().map(mapper::toOrderResponse).toList();
    }
}
