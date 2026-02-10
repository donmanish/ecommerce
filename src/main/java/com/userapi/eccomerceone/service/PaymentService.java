package com.userapi.eccomerceone.service;

import com.userapi.eccomerceone.dto.PaymentRequestDTO;
import com.userapi.eccomerceone.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO createPaymentIntent(PaymentRequestDTO request);
}
