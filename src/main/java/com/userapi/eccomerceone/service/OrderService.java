package com.userapi.eccomerceone.service;

import com.userapi.eccomerceone.dto.OrderRequestDTO;
import com.userapi.eccomerceone.dto.OrderResponseDTO;
import com.userapi.eccomerceone.exceptions.OrderNotFoundException;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);

    OrderResponseDTO getOrderById(Long orderId) throws OrderNotFoundException;

    List<OrderResponseDTO> getAllOrders();
}
