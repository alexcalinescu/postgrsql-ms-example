package com.papel.orders.repository;

import com.papel.orders.entity.Order;
import com.papel.orders.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findById(Long orderItemId);

    List<OrderItem> findAllByOrder(Order order);

}
