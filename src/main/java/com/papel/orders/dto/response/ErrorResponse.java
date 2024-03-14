package com.papel.orders.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}
