package com.userapi.eccomerceone.repository;

import com.userapi.eccomerceone.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
