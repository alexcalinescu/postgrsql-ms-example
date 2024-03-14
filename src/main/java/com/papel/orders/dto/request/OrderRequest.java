package com.papel.orders.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @JsonProperty("order_number")
    @NotBlank
    private String orderNumber;

    @JsonProperty("order_items")
    @NotEmpty
    @Valid
    private List<OrderItemRequest> orderItems;
}
