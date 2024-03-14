package com.papel.orders.exceptions;

import com.papel.orders.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class OrderExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<ErrorResponse> handlerResourceNotFoundException(Exception resourceNotFoundEx) {
        return getResponseEntityError(resourceNotFoundEx, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ ConflictException.class })
    public ResponseEntity<ErrorResponse> handleConflictException(Exception conflictEx) {
        return getResponseEntityError(conflictEx, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception badRequestEx) {
        return getResponseEntityError(badRequestEx, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Error: ", ex);
        ErrorResponse errorResponse = ErrorResponse.builder().errorMessage("Internal server error")
                .timestamp(LocalDateTime.now()).errorCode(500).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> getResponseEntityError(Exception ex, HttpStatus httpStatus) {
        log.error("Error: ", ex);
        ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(ex.getMessage())
                .timestamp(LocalDateTime.now()).errorCode(httpStatus.value()).build();
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}