package org.bazaar.giza.order.controller;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.order.dto.OrderRequest;
import org.bazaar.giza.order.dto.OrderResponse;
import org.bazaar.giza.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok().body(service.create(orderRequest));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> delete(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(service.delete(orderId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(service.getById(orderId));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllByBazaarUserId() {
        Long bazaarUserId = getCurrentBazaarUserId();

        return ResponseEntity.ok().body(service.getAllByBazaarUserId(bazaarUserId));
    }

    private Long getCurrentBazaarUserId() {
        // Implementation as shown earlier
        return null;
    }
}