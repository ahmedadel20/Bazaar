package org.bazaar.giza.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bazaar.giza.order.dto.OrderRequest;
import org.bazaar.giza.order.dto.OrderResponse;
import org.bazaar.giza.order.mapper.OrderMapper;
import org.bazaar.giza.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
@Tag(name = "Orders", description = "Controller for handling mappings for Orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/{customerId}/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderRequest orderRequest, @PathVariable Long customerId) {
        return orderMapper.toOrderResponse(orderService.create(orderMapper.toOrder(orderRequest), customerId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> delete(@Valid @PathVariable Long orderId) {
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
    public List<OrderResponse> getAllByBazaarUserId(@PathVariable Long customerId) {
        return orderService.getAllByBazaarUserId(customerId)
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

}