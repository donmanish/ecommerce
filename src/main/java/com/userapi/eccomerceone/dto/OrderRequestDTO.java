package com.userapi.eccomerceone.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {
    private Long userId;  // The user placing the order
    private List<OrderItemDTO> items;
}
