package com.userapi.eccomerceone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {
    private String paymentIntentId;
    private String clientSecret;
    private String status;
}
