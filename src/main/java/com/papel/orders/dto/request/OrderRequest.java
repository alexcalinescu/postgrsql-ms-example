package com.papel.orders.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.papel.orders.validators.ListNotEmpty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @JsonProperty("order_number")
    @NotBlank(message = "Order Number should not be blank")
    private String orderNumber;

    @JsonProperty("order_items")
    @ListNotEmpty(message = "Order Items should not be empty")
    private List<@Valid OrderItemRequest> orderItems;
}
