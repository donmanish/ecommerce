package com.userapi.eccomerceone.repository;

import com.userapi.eccomerceone.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
