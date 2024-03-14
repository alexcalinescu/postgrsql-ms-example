package com.papel.orders.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {

    @JsonProperty("product_name")
    @NotBlank
    private String productName;

    @JsonProperty("quantity")
    @Min(1)
    private int quantity;

    @JsonProperty("price")
    @Min(0)
    private BigDecimal price;
}
