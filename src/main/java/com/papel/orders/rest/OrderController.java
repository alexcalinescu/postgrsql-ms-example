package com.papel.orders.rest;

import com.papel.orders.dto.request.OrderRequest;
import com.papel.orders.dto.request.StatusUpdateRequest;
import com.papel.orders.dto.response.OrderResponse;
import com.papel.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/papel/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/{orderNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderNumber) {
        return new ResponseEntity<>(orderService.getByOrderNumber(orderNumber), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        orderService.createOrder(orderRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/status/{orderNumber}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateOrderStatus(@RequestBody @Valid StatusUpdateRequest statusUpdateRequest, @PathVariable String orderNumber) {
        orderService.updateStatus(statusUpdateRequest, orderNumber);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
