package com.papel.orders.mappers;

import com.papel.orders.dto.request.OrderItemRequest;
import com.papel.orders.dto.request.OrderRequest;
import com.papel.orders.dto.response.OrderItemResponse;
import com.papel.orders.dto.response.OrderResponse;
import com.papel.orders.entity.Order;
import com.papel.orders.entity.OrderItem;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "getOrderItemsDTO")
    public abstract OrderResponse toDto(Order order);

    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "getOrderItems")
    @Mapping(target = "status", constant = "CREATED")
    public abstract Order toEntity(OrderRequest orderRequest);

    @Named("getOrderItemsDTO")
    List<OrderItemResponse> getOrderItemsDTO(List<OrderItem> orderItems) {
        return orderItems.stream().map(orderItemMapper::toDto).collect(toList());
    }

    @Named("getOrderItems")
    List<OrderItem> getOrderItems(List<OrderItemRequest> orderItemsRequest) {
        return orderItemsRequest.stream().map(orderItemMapper::toEntity).collect(toList());
    }

    @AfterMapping
    void afterMapping(@MappingTarget Order order) {
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));
    }
}
