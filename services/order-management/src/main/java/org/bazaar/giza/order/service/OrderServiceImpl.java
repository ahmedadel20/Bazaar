package org.bazaar.giza.order.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.order.dto.OrderRequest;
import org.bazaar.giza.order.dto.OrderResponse;
import org.bazaar.giza.order.entity.Order;
import org.bazaar.giza.order.exception.OrderEmptyException;
import org.bazaar.giza.order.exception.OrderNotFoundException;
import org.bazaar.giza.order.mapper.OrderMapper;
import org.bazaar.giza.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse create(OrderRequest orderRequest) {
        Order order = orderRepository.save(orderMapper.toOrder(orderRequest));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public String delete(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException(orderId);
        }
        orderRepository.deleteById(orderId);
        return "Order deleted";
    }

    @Override
    public OrderResponse getById(Long orderId) {
        return orderRepository.findById(orderId).map(orderMapper::toOrderResponse).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public List<OrderResponse> getAllByBazaarUserId(Long bazaarUserId) {
        var orders = orderRepository.findAllByBazaarUserId(bazaarUserId);
        if (orders.isEmpty()) {
            throw new OrderEmptyException(bazaarUserId);
        }
        return orderRepository.findAllByBazaarUserId(bazaarUserId).stream().map(orderMapper::toOrderResponse).toList();
    }
}
