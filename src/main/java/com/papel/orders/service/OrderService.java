package com.papel.orders.service;

import com.papel.orders.dto.request.OrderRequest;
import com.papel.orders.dto.response.OrderResponse;
import com.papel.orders.entity.Order;
import com.papel.orders.exceptions.ResourceNotFoundException;
import com.papel.orders.mappers.OrderMapper;
import com.papel.orders.repository.OrderRepository;
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

    public OrderResponse getOrderById(long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("Order not found");
        }
        return orderMapper.toDto(orderOptional.get());
    }

    public void createOrder(OrderRequest orderRequest) {
        Order order = orderMapper.toEntity(orderRequest);
        orderRepository.save(order);
    }
}
