package com.papel.orders.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.papel.orders.entity.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {

    @JsonProperty("status")
    @Valid
    @NotNull(message = "Status should not be null")
    private OrderStatus status;

    @JsonProperty("version")
    @Min(value = 0, message = "Version min value is 0")
    private Integer version;

}
