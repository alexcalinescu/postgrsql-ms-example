package com.papel.orders.service;

import com.papel.orders.dto.request.OrderRequest;
import com.papel.orders.dto.request.StatusUpdateRequest;
import com.papel.orders.dto.response.OrderResponse;
import com.papel.orders.entity.Order;
import com.papel.orders.exceptions.ConflictException;
import com.papel.orders.exceptions.ResourceNotFoundException;
import com.papel.orders.mappers.OrderMapper;
import com.papel.orders.repository.OrderRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderMapper orderMapper;

    private final OrderRepository orderRepository;

    public OrderService(OrderMapper orderMapper, OrderRepository orderRepository) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
    }

    public OrderResponse getByOrderNumber(String orderNumber) {
        Optional<Order> orderOptional = orderRepository.findByOrderNumber(orderNumber);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Order not found for orderNumber: %s", orderNumber));
        }
        return orderMapper.toDto(orderOptional.get());
    }

    public void createOrder(OrderRequest orderRequest) {
        Order order = orderMapper.toEntity(orderRequest);
        orderRepository.save(order);
    }

    public void updateStatus(StatusUpdateRequest statusUpdateRequest, String orderNumber) {
        Optional<Order> orderOptional = orderRepository.findByOrderNumber(orderNumber);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Order not found for orderNumber: %s", orderNumber));
        }
        Order order = orderOptional.get();
        if (!order.getVersion().equals(statusUpdateRequest.getVersion())) {
            throw new ConflictException("Version provided is not the latest");
        }
        order.setStatus(statusUpdateRequest.getStatus());
        order.setVersion(statusUpdateRequest.getVersion());
        try {
            orderRepository.save(order);
        } catch (OptimisticLockingFailureException ex) {
            throw new ConflictException("Version provided is not the latest");
        }
    }
}
