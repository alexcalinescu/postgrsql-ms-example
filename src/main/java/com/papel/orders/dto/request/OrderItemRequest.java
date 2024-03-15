package com.papel.orders.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {

    @JsonProperty("product_name")
    @NotBlank(message = "Product name should not be blank")
    private String productName;

    @JsonProperty("quantity")
    @Min(value = 1, message = "Quantity min value is 1")
    private int quantity;

    @JsonProperty("price")
    @Min(value = 0, message = "Price should be greater than 0")
    private BigDecimal price;
}
