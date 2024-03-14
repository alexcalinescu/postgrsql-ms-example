package com.papel.orders.integration;

import com.papel.orders.dto.request.OrderItemRequest;
import com.papel.orders.dto.request.OrderRequest;
import com.papel.orders.dto.request.StatusUpdateRequest;
import com.papel.orders.dto.response.OrderItemResponse;
import com.papel.orders.dto.response.OrderResponse;
import com.papel.orders.entity.Order;
import com.papel.orders.entity.OrderItem;
import com.papel.orders.entity.OrderStatus;
import com.papel.orders.exceptions.ConflictException;
import com.papel.orders.exceptions.ResourceNotFoundException;
import com.papel.orders.repository.OrderRepository;
import com.papel.orders.service.OrderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ComponentTest")
@SpringBootTest
@DirtiesContext
@Transactional
public class OrderServiceTest {

    private static final String ORDER_NUMBER = "RO-XYZ-0129";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private Order existingOrder;

    @BeforeEach
    private void initOrder() {
        existingOrder = new Order();
        existingOrder.setOrderId(1L);
        existingOrder.setStatus(OrderStatus.CREATED);
        existingOrder.setOrderNumber(ORDER_NUMBER);
        existingOrder.setVersion(0);
        OrderItem existingOrderItem = new OrderItem();
        existingOrderItem.setOrder(existingOrder);
        existingOrderItem.setOrderItemId(1L);
        existingOrderItem.setProductName("Printer");
        existingOrderItem.setQuantity(1);
        existingOrderItem.setUnitPrice(BigDecimal.valueOf(349.99));
        existingOrder.setOrderItems(List.of(existingOrderItem));
    }

    @Test
    public void testGetByOrderNumberSuccessfully() {
        orderRepository.save(existingOrder);

        OrderResponse actualOrderResponse = orderService.getByOrderNumber(ORDER_NUMBER);

        assertEquals(ORDER_NUMBER, actualOrderResponse.getOrderNumber());
        assertEquals(OrderStatus.CREATED, actualOrderResponse.getStatus());
        assertNotNull(actualOrderResponse.getOrderDate());
        assertEquals(existingOrder.getVersion(), actualOrderResponse.getVersion());
        OrderItem orderItem = existingOrder.getOrderItems().get(0);
        OrderItemResponse orderItemResponse = actualOrderResponse.getOrderItems().get(0);
        assertEquals(orderItem.getUnitPrice(), orderItemResponse.getPrice());
        assertEquals(orderItem.getProductName(), orderItemResponse.getProductName());
        assertEquals(orderItem.getQuantity(), orderItemResponse.getQuantity());
    }

    @Test
    public void testGetByOrderNumberMissingOrder() {
        assertThrows(ResourceNotFoundException.class, () -> orderService.getByOrderNumber(ORDER_NUMBER));
    }

    @Test
    public void testCreateOrderSuccessfully() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderNumber(ORDER_NUMBER);
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductName("Printer");
        orderItemRequest.setQuantity(1);
        orderItemRequest.setPrice(BigDecimal.valueOf(349.99));
        orderRequest.setOrderItems(List.of(orderItemRequest));

        orderService.createOrder(orderRequest);

        Optional<Order> retrievedOrderOptional = orderRepository.findByOrderNumber(ORDER_NUMBER);
        assertTrue(retrievedOrderOptional.isPresent());
    }

    @Test
    public void testUpdateStatusSuccessfully() {
        orderRepository.save(existingOrder);
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
        statusUpdateRequest.setStatus(OrderStatus.DELIVERED);
        statusUpdateRequest.setVersion(existingOrder.getVersion());

        orderService.updateStatus(statusUpdateRequest, ORDER_NUMBER);

        Optional<Order> retrievedOrderOptional = orderRepository.findByOrderNumber(ORDER_NUMBER);
        assertTrue(retrievedOrderOptional.isPresent());
        Order retrievedOrder = retrievedOrderOptional.get();
        assertEquals(OrderStatus.DELIVERED, retrievedOrder.getStatus());
        assertEquals(existingOrder.getVersion() + 1, retrievedOrder.getVersion());
    }

    @Test
    public void testUpdateStatusWrongVersion() {
        orderRepository.save(existingOrder);
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
        statusUpdateRequest.setStatus(OrderStatus.DELIVERED);
        statusUpdateRequest.setVersion(existingOrder.getVersion() + 1);

        assertThrows(ConflictException.class, () -> orderService.updateStatus(statusUpdateRequest, ORDER_NUMBER));
    }

}
