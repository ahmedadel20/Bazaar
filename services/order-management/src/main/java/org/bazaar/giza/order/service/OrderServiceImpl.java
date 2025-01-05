package org.bazaar.giza.order.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.clients.CartItemResponse;
import org.bazaar.giza.clients.InventoryClient;
import org.bazaar.giza.exception.InventoryNotAvailableException;
import org.bazaar.giza.order.entity.Order;
import org.bazaar.giza.order.exception.DuplicateOrderException;
import org.bazaar.giza.order.exception.OrderEmptyException;
import org.bazaar.giza.order.exception.OrderNotFoundException;
import org.bazaar.giza.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @Override
    @Transactional
    public Order create(Order order, Long customerId) {
        if (orderRepository.findById(order.getId()).isPresent()) {
            throw new DuplicateOrderException(order.getId());
        }

        order.setId(null);

        // Retrieve cart items for the user
        List<CartItemResponse> cartItemResponses = inventoryClient.getCart(customerId).getBody();
        if (cartItemResponses == null)
            throw new InventoryNotAvailableException();
        if (cartItemResponses.isEmpty())
            throw new OrderEmptyException(order.getBazaarUserId());
        // Calculate the total price dynamically
        BigDecimal totalPrice = cartItemResponses
                .stream()
                .map(
                        item -> item.productDto()
                                .currentPrice()
                                .multiply(new BigDecimal(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum up the total price

        // Save the price
        order.setFinalPrice(totalPrice);

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Clear the cart once the order is placed
        String cartCleared = inventoryClient.clearCart(customerId).getBody();
        if (cartCleared == null)
            throw new InventoryNotAvailableException();

        return savedOrder;
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
    public Order getById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public List<Order> getAllByBazaarUserId(Long bazaarUserId) {
        return orderRepository
                .findAllByBazaarUserId(bazaarUserId)
                .stream()
                .toList();
    }
}
