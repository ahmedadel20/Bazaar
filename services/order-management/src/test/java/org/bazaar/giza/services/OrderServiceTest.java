package org.bazaar.giza.services;

import org.bazaar.giza.clients.CartItemResponse;
import org.bazaar.giza.clients.InventoryClient;
import org.bazaar.giza.clients.ProductDto;
import org.bazaar.giza.exception.InventoryNotAvailableException;
import org.bazaar.giza.order.entity.Order;
import org.bazaar.giza.order.exception.OrderEmptyException;
import org.bazaar.giza.order.exception.OrderNotFoundException;
import org.bazaar.giza.order.mapper.OrderMapper;
import org.bazaar.giza.order.repository.OrderRepository;
import org.bazaar.giza.order.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderServiceTest {
        @Mock
        private OrderRepository orderRepo;
        @Mock
        private OrderMapper orderMapper;
        @Mock
        private InventoryClient inventory;
        @InjectMocks
        private OrderServiceImpl orderService;

        private static Order existingOrder;
        private static Order newOrder;
        private static List<CartItemResponse> userCartItems;

        @BeforeEach
        void setup() {
                existingOrder = Order.builder()
                                .id(1L)
                                .bazaarUserId(1L)
                                .description("Test Existing Order")
                                .finalPrice(BigDecimal.TEN)
                                .orderDate(new Date(System.currentTimeMillis()))
                                .build();
                newOrder = Order.builder()
                                .id(2L)
                                .bazaarUserId(1L)
                                .description("Test New Order")
                                .finalPrice(BigDecimal.TEN)
                                .orderDate(new Date(System.currentTimeMillis()))
                                .build();

                userCartItems = List.of(
                                CartItemResponse.builder()
                                                .id(1L)
                                                .bazaarUserId(1L)
                                                .productDto(ProductDto.builder().currentPrice(BigDecimal.TEN).build())
                                                .quantity(1)
                                                .build(),
                                CartItemResponse.builder()
                                                .id(2L)
                                                .bazaarUserId(1L)
                                                .productDto(ProductDto.builder().currentPrice(BigDecimal.TEN).build())
                                                .quantity(2)
                                                .build());
        }

        @Test
        void testCreatingNewOrder() {
                Mockito.when(orderRepo.findById(newOrder.getId()))
                                .thenReturn(Optional.empty());
                Mockito.when(orderRepo.save(newOrder))
                                .then(i -> {
                                        newOrder.setId(1L);
                                        return newOrder;
                                });
                Mockito.when(inventory.getCart(newOrder.getBazaarUserId()))
                                .thenReturn(ResponseEntity.of(Optional.of(userCartItems)));
                Mockito.when(inventory.clearCart(newOrder.getBazaarUserId()))
                                .thenReturn(ResponseEntity.of(Optional.of("Cart cleared")));

                Order returnedOrder = Order.builder()
                                .id(1L) // differs
                                .bazaarUserId(1L)
                                .description("Test New Order")
                                .finalPrice(BigDecimal.valueOf(30))
                                .orderDate(new Date(System.currentTimeMillis()))
                                .build();

                assert (compareOrder(returnedOrder, orderService.create(newOrder, newOrder.getBazaarUserId())));
        }

        @Test
        void testCreatingNewOrder_WithNoCartItems() {
                Mockito.when(orderRepo.findById(newOrder.getId()))
                                .thenReturn(Optional.empty());
                Mockito.when(orderRepo.save(newOrder))
                                .then(i -> {
                                        newOrder.setId(1L);
                                        return newOrder;
                                });
                Mockito.when(inventory.getCart(newOrder.getBazaarUserId()))
                                .thenReturn(ResponseEntity.of(Optional.of(List.of())));
                Mockito.when(inventory.clearCart(newOrder.getBazaarUserId()))
                                .thenReturn(ResponseEntity.of(Optional.of("Cart cleared")));

                assertThrows(
                                OrderEmptyException.class,
                                () -> orderService.create(newOrder, newOrder.getBazaarUserId()));
        }

        @Test
        void testCreatingOrder_InventoryNotAvailable() {
                Mockito.when(orderRepo.findById(newOrder.getId()))
                                .thenReturn(Optional.empty());
                Mockito.when(orderRepo.save(newOrder))
                                .then(i -> {
                                        newOrder.setId(1L);
                                        return newOrder;
                                });
                Mockito.when(inventory.getCart(newOrder.getBazaarUserId()))
                                .thenReturn(ResponseEntity.of(Optional.empty()));
                Mockito.when(inventory.clearCart(newOrder.getBazaarUserId()))
                                .thenReturn(ResponseEntity.of(Optional.of("Cart cleared")));

                assertThrows(
                                InventoryNotAvailableException.class,
                                () -> orderService.create(newOrder, newOrder.getBazaarUserId()));
        }

        @Test
        void testFindingExistingOrder() {
                Mockito.when(orderRepo.findById(existingOrder.getId()))
                                .thenReturn(Optional.of(existingOrder));
                assert (compareOrder(existingOrder, orderService.getById(existingOrder.getId())));
        }

        @Test
        void testFindingNonExistingOrder() {
                Mockito.when(orderRepo.findById(newOrder.getId()))
                                .thenReturn(Optional.empty());
                assertThrows(
                                OrderNotFoundException.class,
                                () -> orderService.getById(newOrder.getId()));
        }

        @Test
        void testDeletingOrder() {
                Mockito.when(orderRepo.existsById(existingOrder.getId()))
                                .thenReturn(true);
                assertEquals(
                                "Order deleted",
                                orderService.delete(existingOrder.getId()));
        }

        @Test
        void testDeletingNonExistingOrder() {
                Mockito.when(orderRepo.existsById(existingOrder.getId()))
                                .thenReturn(false);
                assertThrows(
                                OrderNotFoundException.class,
                                () -> orderService.delete(existingOrder.getId()));
        }

        @Test
        void testFindingAllByUserId() {
                Mockito.when(orderRepo.findAllByBazaarUserId(existingOrder.getBazaarUserId()))
                                .thenReturn(List.of(existingOrder));
                var expectedOrdersList = List.of(existingOrder);
                var retunredOrdersList = orderService.getAllByBazaarUserId(existingOrder.getBazaarUserId());
                assertEquals(
                                retunredOrdersList.size(),
                                expectedOrdersList.size());
                for (int i = 0; i < expectedOrdersList.size(); i++) {
                        assert (compareOrder(expectedOrdersList.get(i), retunredOrdersList.get(i)));
                }
        }

        @Test
        void testFindingAllOrders_NoneExistingUser() {
                Mockito.when(orderRepo.findAllByBazaarUserId(existingOrder.getBazaarUserId()))
                                .thenReturn(List.of());
                var retunredOrdersList = orderService.getAllByBazaarUserId(existingOrder.getBazaarUserId());
                assertEquals(
                                0,
                                retunredOrdersList.size());
        }

        private boolean compareOrder(Order order1, Order order2) {
                return (order1.getId().compareTo(order2.getId()) == 0) &&
                                (order1.getBazaarUserId().compareTo(order2.getBazaarUserId()) == 0) &&
                                (order1.getDescription().equals(order2.getDescription())) &&
                                (order1.getFinalPrice().compareTo(order2.getFinalPrice()) == 0);
        }
}
