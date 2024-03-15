package com.papel.orders.service;

import com.papel.orders.dto.request.OrderRequest;
import com.papel.orders.dto.request.StatusUpdateRequest;
import com.papel.orders.dto.response.OrderResponse;
import com.papel.orders.entity.Order;
import com.papel.orders.exceptions.BadRequestException;
import com.papel.orders.exceptions.ConflictException;
import com.papel.orders.exceptions.ResourceNotFoundException;
import com.papel.orders.mappers.OrderMapper;
import com.papel.orders.repository.OrderRepository;
import com.papel.orders.validators.OrderStatusValidator;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final OrderMapper orderMapper;

    private final OrderRepository orderRepository;

    private final OrderStatusValidator orderStatusValidator;

    public OrderService(OrderMapper orderMapper, OrderRepository orderRepository, OrderStatusValidator orderStatusValidator) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.orderStatusValidator = orderStatusValidator;
    }

    public OrderResponse getByOrderNumber(String orderNumber) {
        Optional<Order> orderOptional = orderRepository.findByOrderNumber(orderNumber);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Order not found for orderNumber: %s", orderNumber));
        }
        return orderMapper.toDto(orderOptional.get());
    }

    public void createOrder(OrderRequest orderRequest) {
        Optional<Order> orderOptional = orderRepository.findByOrderNumber(orderRequest.getOrderNumber());
        if (orderOptional.isPresent()) {
            throw new ConflictException(String.format("Order with orderNumber: %s already exists", orderRequest.getOrderNumber()));
        }
        Order order = orderMapper.toEntity(orderRequest);
        orderRepository.save(order);
    }

    public void updateStatus(StatusUpdateRequest statusUpdateRequest, String orderNumber) {
        Optional<Order> orderOptional = orderRepository.findByOrderNumber(orderNumber);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Order not found for orderNumber: %s", orderNumber));
        }
        Order order = orderOptional.get();
        if (!orderStatusValidator.isTargetedStatusAccepted(statusUpdateRequest.getStatus(), order.getStatus())) {
            throw new BadRequestException(String.format("Order status cannot be changed from %s to %s", order.getStatus(), statusUpdateRequest.getStatus()));
        }
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
