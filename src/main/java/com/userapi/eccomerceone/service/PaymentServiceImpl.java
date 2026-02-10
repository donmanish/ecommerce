package com.userapi.eccomerceone.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.userapi.eccomerceone.dto.PaymentRequestDTO;
import com.userapi.eccomerceone.dto.PaymentResponseDTO;
import com.userapi.eccomerceone.model.Order;
import com.userapi.eccomerceone.model.Payment;
import com.userapi.eccomerceone.model.PaymentStatus;
import com.userapi.eccomerceone.repository.OrderRepository;
import com.userapi.eccomerceone.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponseDTO createPaymentIntent(PaymentRequestDTO request) {
        try {
            // 1️⃣ Fetch order
            Order order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // 2️⃣ Convert amount to cents for Stripe
            long amountInCents = order.getTotalAmount().multiply(BigDecimal.valueOf(100)).longValue();

            // 3️⃣ Create Stripe PaymentIntent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(request.getCurrency())
                    .addPaymentMethodType("card")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // 4️⃣ Save payment in DB
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            payment.setCurrency(request.getCurrency());
            payment.setStripePaymentIntentId(paymentIntent.getId());
            payment.setStatus(PaymentStatus.INITIATED);

            paymentRepository.save(payment);

            // 5️⃣ Build response
            PaymentResponseDTO response = new PaymentResponseDTO();
            response.setPaymentIntentId(paymentIntent.getId());
            response.setClientSecret(paymentIntent.getClientSecret());
            response.setStatus(payment.getStatus().name());

            return response;

        } catch (StripeException e) {
            throw new RuntimeException("Stripe error: " + e.getMessage());
        }
    }
}
