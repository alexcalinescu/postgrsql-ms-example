package com.papel.orders.unit;

import com.papel.orders.dto.request.OrderRequest;
import com.papel.orders.dto.request.StatusUpdateRequest;
import com.papel.orders.dto.response.OrderResponse;
import com.papel.orders.entity.Order;
import com.papel.orders.entity.OrderStatus;
import com.papel.orders.exceptions.BadRequestException;
import com.papel.orders.exceptions.ConflictException;
import com.papel.orders.exceptions.ResourceNotFoundException;
import com.papel.orders.mappers.OrderMapper;
import com.papel.orders.repository.OrderRepository;
import com.papel.orders.service.OrderService;
import com.papel.orders.validators.OrderStatusValidator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private static final String ORDER_NUMBER = "RO-XYZ-0129";

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderStatusValidator orderStatusValidator;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testGetByOrderNumberSuccessfully() {
        Order order = new Order();
        OrderResponse expectedOrderResponse = new OrderResponse();

        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(expectedOrderResponse);

        OrderResponse actualOrderResponse = orderService.getByOrderNumber(ORDER_NUMBER);

        verify(orderRepository, times(1)).findByOrderNumber(ORDER_NUMBER);
        verify(orderMapper, times(1)).toDto(order);
        assertSame(expectedOrderResponse, actualOrderResponse);
    }

    @Test
    public void testGetByOrderNumberMissingOrder() {
        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getByOrderNumber(ORDER_NUMBER));
    }

    @Test
    public void testCreateOrderSuccessfully() {
        OrderRequest orderRequest = new OrderRequest();
        Order order = new Order();

        when(orderMapper.toEntity(orderRequest)).thenReturn(order);

        orderService.createOrder(orderRequest);

        verify(orderMapper, times(1)).toEntity(orderRequest);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testCreateExistingOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderNumber(ORDER_NUMBER);
        Order order = new Order();
        order.setOrderNumber(ORDER_NUMBER);

        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(Optional.of(order));

        assertThrows(ConflictException.class, () -> orderService.createOrder(orderRequest));
    }

    @Test
    public void testUpdateStatusSuccessfully() {
        Order order = new Order();
        order.setVersion(0);
        order.setStatus(OrderStatus.CREATED);
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
        statusUpdateRequest.setStatus(OrderStatus.DELIVERED);
        statusUpdateRequest.setVersion(order.getVersion());

        when(orderStatusValidator.isTargetedStatusAccepted(OrderStatus.DELIVERED, OrderStatus.CREATED)).thenReturn(true);
        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(Optional.of(order));

        orderService.updateStatus(statusUpdateRequest, ORDER_NUMBER);

        verify(orderRepository, times(1)).findByOrderNumber(ORDER_NUMBER);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testUpdateStatusWrongVersion() {
        Order order = new Order();
        order.setVersion(0);
        order.setStatus(OrderStatus.CREATED);
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
        statusUpdateRequest.setStatus(OrderStatus.PENDING);
        statusUpdateRequest.setVersion(order.getVersion() + 1);

        when(orderStatusValidator.isTargetedStatusAccepted(OrderStatus.PENDING, OrderStatus.CREATED)).thenReturn(true);
        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(Optional.of(order));

        assertThrows(ConflictException.class, () -> orderService.updateStatus(statusUpdateRequest, ORDER_NUMBER));
    }

    @Test
    public void testUpdateStatusWrongTargetedStatus() {
        Order order = new Order();
        order.setVersion(0);
        order.setStatus(OrderStatus.CREATED);
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
        statusUpdateRequest.setStatus(OrderStatus.CREATED);
        statusUpdateRequest.setVersion(order.getVersion());

        when(orderStatusValidator.isTargetedStatusAccepted(OrderStatus.CREATED, OrderStatus.CREATED)).thenReturn(false);
        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () -> orderService.updateStatus(statusUpdateRequest, ORDER_NUMBER));
    }

    @Test
    public void testUpdateStatusThrowOptimisticLockException() {
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
        statusUpdateRequest.setVersion(0);
        statusUpdateRequest.setStatus(OrderStatus.DELIVERED);
        Order order = new Order();
        order.setVersion(0);
        order.setStatus(OrderStatus.CREATED);

        when(orderStatusValidator.isTargetedStatusAccepted(OrderStatus.DELIVERED, OrderStatus.CREATED)).thenReturn(true);
        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(Optional.of(order));
        doThrow(OptimisticLockingFailureException.class).when(orderRepository).save(order);

        assertThrows(ConflictException.class, () -> orderService.updateStatus(statusUpdateRequest, ORDER_NUMBER));
    }
}
