package com.papel.orders.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.papel.orders.entity.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {

    @JsonProperty("status")
    @NotNull
    private OrderStatus status;

    @JsonProperty("version")
    @Min(0)
    private Integer version;

}
