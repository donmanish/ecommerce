package com.userapi.eccomerceone.model;

//OrderItem
//- order (ManyToOne)
//- product (ManyToOne)
//- quantity
//- priceAtPurchase


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem extends BaseModel{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)

    private Product product;

    @Column(nullable = false)
    private Integer quantity ;

    @Column(nullable = false)
    private BigDecimal priceAtPurchase;
}
