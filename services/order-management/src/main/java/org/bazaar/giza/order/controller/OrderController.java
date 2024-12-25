package org.bazaar.giza.order.controller;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.order.dto.OrderRequest;
import org.bazaar.giza.order.dto.OrderResponse;
import org.bazaar.giza.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest orderRequest) {
        Long currentBazaarUserId = getCurrentBazaarUserId();
        return new ResponseEntity<>(orderService.create(orderRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> delete(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.delete(orderId), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getById(orderId), HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllByBazaarUserId() {
        Long bazaarUserId = getCurrentBazaarUserId();

        return new ResponseEntity<>(orderService.getAllByBazaarUserId(bazaarUserId), HttpStatus.FOUND);
    }

    private Long getCurrentBazaarUserId() {
        // Implementation as shown earlier
        return null;
    }
}