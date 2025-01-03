package org.bazaar.giza.order.controller;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.order.dto.OrderRequest;
import org.bazaar.giza.order.dto.OrderResponse;
import org.bazaar.giza.order.mapper.OrderMapper;
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
    private final OrderMapper orderMapper;
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@RequestBody OrderRequest orderRequest) {
        return orderMapper.toOrderResponse(orderService.create(orderMapper.toOrder(orderRequest)));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> delete(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.delete(orderId), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.FOUND)
    public OrderResponse getById(@PathVariable Long orderId) {
        return orderMapper.toOrderResponse(orderService.getById(orderId));
    }

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.FOUND)
    public List<OrderResponse> getAllByBazaarUserId() {
        Long bazaarUserId = getCurrentBazaarUserId();
        return orderService.getAllByBazaarUserId(bazaarUserId)
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    private Long getCurrentBazaarUserId() {
        // Implementation as shown earlier
        return 1L;
    }
}