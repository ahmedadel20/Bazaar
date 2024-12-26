package org.bazaar.giza.order.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.cartItem.dto.CartItemResponse;
import org.bazaar.giza.cartItem.service.CartItemService;
import org.bazaar.giza.order.dto.OrderRequest;
import org.bazaar.giza.order.dto.OrderResponse;
import org.bazaar.giza.order.entity.Order;
import org.bazaar.giza.order.exception.DuplicateOrderException;
import org.bazaar.giza.order.exception.OrderNotFoundException;
import org.bazaar.giza.order.mapper.OrderMapper;
import org.bazaar.giza.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartItemService cartItemService;

    @Override
    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        if(orderRepository.findById(orderRequest.id()).isPresent()) {
            throw new DuplicateOrderException(orderRequest.id());
        }

        Long currentBazaarUserId = getCurrentBazaarUserId();
        Order order = orderMapper.toOrder(orderRequest);

        order.setId(null);

        // Retrieve cart items for the user
        List<CartItemResponse> cartItemResponses = cartItemService.getCart(currentBazaarUserId);

        // Calculate the total price dynamically
        BigDecimal totalPrice = cartItemResponses.stream()
                .map(item -> item.currentPrice().multiply(new BigDecimal(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum up the total price

        // Save the price
        order.setFinalPrice(totalPrice);

        //Save the order
        orderRepository.save(order);

        // Clear the cart once the order is placed
        cartItemService.clearCart(currentBazaarUserId);

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

//    public List<OrderResponse> getOrdersByStatus(Long bazaarUserId, String status) {
//        return orderRepository.findAllByBazaarUserIdAndStatus(bazaarUserId, status)
//                .stream()
//                .map(orderMapper::toOrderResponse)
//                .toList();
//    }

    @Override
    public List<OrderResponse> getAllByBazaarUserId(Long bazaarUserId) {
        return orderRepository.findAllByBazaarUserId(bazaarUserId).stream().map(orderMapper::toOrderResponse).toList();
    }

    private Long getCurrentBazaarUserId() {
        // Implementation as shown earlier
        return null;
    }
}
