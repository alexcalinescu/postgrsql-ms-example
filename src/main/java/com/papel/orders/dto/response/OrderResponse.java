package com.papel.orders.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.papel.orders.entity.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

    @JsonProperty("order_number")
    private String orderNumber;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("order_date")
    private LocalDateTime orderDate;

    @JsonProperty("order_items")
    private List<OrderItemResponse> orderItems;
}
