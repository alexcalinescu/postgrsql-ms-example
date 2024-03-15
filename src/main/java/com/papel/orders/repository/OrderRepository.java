package com.papel.orders.repository;

import com.papel.orders.entity.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Order> findByOrderNumber(String orderNumber);

}
