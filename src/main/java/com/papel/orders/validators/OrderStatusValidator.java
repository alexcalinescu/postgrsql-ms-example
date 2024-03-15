package com.papel.orders.validators;

import com.papel.orders.entity.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OrderStatusValidator {

    private static final Map<OrderStatus, List<OrderStatus>> TARGETED_STATUS_TO_ACCEPTED_STATUSES = Map.of(
            OrderStatus.CREATED, List.of(),
            OrderStatus.PENDING, List.of(OrderStatus.CREATED),
            OrderStatus.CANCELLED, List.of(OrderStatus.CREATED, OrderStatus.PENDING),
            OrderStatus.DELIVERED, List.of(OrderStatus.CREATED, OrderStatus.PENDING));

    public boolean isTargetedStatusAccepted(OrderStatus targetedStatus, OrderStatus existingStatus) {
        return TARGETED_STATUS_TO_ACCEPTED_STATUSES.get(targetedStatus).contains(existingStatus);
    }
}
