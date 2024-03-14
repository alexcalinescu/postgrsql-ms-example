package com.papel.orders.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("quality")
    private int quantity;

    @JsonProperty("price")
    private BigDecimal price;
}
