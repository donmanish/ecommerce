package com.userapi.eccomerceone.controller;

import com.userapi.eccomerceone.dto.OrderRequestDTO;
import com.userapi.eccomerceone.dto.OrderResponseDTO;
import com.userapi.eccomerceone.exceptions.OrderNotFoundException;
import com.userapi.eccomerceone.exceptions.UnauthorizedException;
import com.userapi.eccomerceone.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order APIs", description = "Operations for managing orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequestDTO orderRequestDTO)  throws UnauthorizedException {

        if (!isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated. Please login first.");
        }


        try {
            OrderResponseDTO response = orderService.createOrder(orderRequestDTO);
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Order created successfully");
            map.put("data", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(map);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Failed to create order");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<Object> getOrder(@PathVariable Long id) throws UnauthorizedException {

        if (!isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated. Please login first.");
        }

        try {
            OrderResponseDTO response = orderService.getOrderById(id);
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Order fetched successfully");
            map.put("data", response);
            return ResponseEntity.ok(map);
        } catch (OrderNotFoundException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Failed to fetch order");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping
    @Operation(summary = "List all orders")
    public ResponseEntity<Object> getAllOrders() throws UnauthorizedException {
        if (!isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated. Please login first.");
        }

        try {
            List<OrderResponseDTO> orders = orderService.getAllOrders();
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Orders fetched successfully");
            map.put("data", orders);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Failed to fetch orders");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
