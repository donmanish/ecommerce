package com.userapi.eccomerceone.service;

import com.userapi.eccomerceone.dto.*;
import com.userapi.eccomerceone.exceptions.OrderNotFoundException;
import com.userapi.eccomerceone.model.*;
import com.userapi.eccomerceone.repository.OrderItemRepository;
import com.userapi.eccomerceone.repository.OrderRepository;
import com.userapi.eccomerceone.repository.ProductRepository;
import com.userapi.eccomerceone.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                            ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        try {
            // Fetch user
            User user = userRepository.findById(orderRequestDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create order
            Order order = new Order();
            order.setUser(user);
            order.setStatus(OrderStatus.CREATED);

            // Calculate total amount and create OrderItems
            final BigDecimal[] totalAmount = {BigDecimal.ZERO};

            List<OrderItem> items = orderRequestDTO.getItems().stream().map(itemDTO -> {
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDTO.getProductId()));

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(itemDTO.getQuantity());

                // Convert price to BigDecimal
                BigDecimal price = BigDecimal.valueOf(product.getPrice());
                BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(itemDTO.getQuantity()));

                orderItem.setPriceAtPurchase(totalPrice);

                // Add to total
                totalAmount[0] = totalAmount[0].add(totalPrice);

                return orderItem;
            }).collect(Collectors.toList());

            order.setTotalAmount(totalAmount[0]);
            order.setOrderItems(items);

            // Save order + items (cascade)
            Order savedOrder = orderRepository.save(order);

            // Map to response DTO
            OrderResponseDTO responseDTO = new OrderResponseDTO();
            responseDTO.setOrderId(savedOrder.getId());
            responseDTO.setUserId(savedOrder.getUser().getId());
            responseDTO.setTotalAmount(savedOrder.getTotalAmount());
            responseDTO.setStatus(savedOrder.getStatus().name());

            List<OrderItemResponseDTO> itemResponses = savedOrder.getOrderItems().stream().map(oi -> {
                OrderItemResponseDTO dto = new OrderItemResponseDTO();
                dto.setProductId(oi.getProduct().getId());
                dto.setProductName(oi.getProduct().getTitle());
                dto.setQuantity(oi.getQuantity());
                dto.setPriceAtPurchase(oi.getPriceAtPurchase());
                return dto;
            }).collect(Collectors.toList());

            responseDTO.setOrderItems(itemResponses);

            return responseDTO;

        } catch (Exception e) {
            // Handle exception properly
            e.printStackTrace();
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }
    }


    @Override
    public OrderResponseDTO getOrderById(Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderId(order.getId());
        responseDTO.setUserId(order.getUser().getId());
        responseDTO.setTotalAmount(order.getTotalAmount());
        responseDTO.setStatus(order.getStatus().name());

        List<OrderItemResponseDTO> itemResponses = order.getOrderItems().stream().map(oi -> {
            OrderItemResponseDTO dto = new OrderItemResponseDTO();
            dto.setProductId(oi.getProduct().getId());
            dto.setProductName(oi.getProduct().getTitle());
            dto.setQuantity(oi.getQuantity());
            dto.setPriceAtPurchase(oi.getPriceAtPurchase());
            return dto;
        }).collect(Collectors.toList());

        responseDTO.setOrderItems(itemResponses);

        return responseDTO;
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> {
            OrderResponseDTO responseDTO = new OrderResponseDTO();
            responseDTO.setOrderId(order.getId());
            responseDTO.setUserId(order.getUser().getId());
            responseDTO.setTotalAmount(order.getTotalAmount());
            responseDTO.setStatus(order.getStatus().name());

            List<OrderItemResponseDTO> itemResponses = order.getOrderItems().stream().map(oi -> {
                OrderItemResponseDTO dto = new OrderItemResponseDTO();
                dto.setProductId(oi.getProduct().getId());
                dto.setProductName(oi.getProduct().getTitle());
                dto.setQuantity(oi.getQuantity());
                dto.setPriceAtPurchase(oi.getPriceAtPurchase());
                return dto;
            }).collect(Collectors.toList());

            responseDTO.setOrderItems(itemResponses);

            return responseDTO;
        }).collect(Collectors.toList());
    }
}
