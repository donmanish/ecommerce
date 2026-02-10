package com.userapi.eccomerceone.repository;


import com.userapi.eccomerceone.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
