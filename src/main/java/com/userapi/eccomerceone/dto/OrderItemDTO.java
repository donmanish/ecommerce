package com.userapi.eccomerceone.dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderItemDTO {
    private Long productId;
    private Integer quantity;
}
