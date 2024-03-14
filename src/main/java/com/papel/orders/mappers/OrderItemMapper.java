package com.papel.orders.mappers;

import com.papel.orders.dto.request.OrderItemRequest;
import com.papel.orders.dto.response.OrderItemResponse;
import com.papel.orders.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "price", source = "unitPrice")
    OrderItemResponse toDto(OrderItem orderItem);

    @Mapping(target = "unitPrice", source = "price")
    OrderItem toEntity(OrderItemRequest orderItemRequest);
}
