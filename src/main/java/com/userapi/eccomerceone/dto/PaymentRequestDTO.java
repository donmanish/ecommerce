package com.userapi.eccomerceone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {
    private Long orderId;   // Order to pay
    private String currency; // e.g., "usd"
}
