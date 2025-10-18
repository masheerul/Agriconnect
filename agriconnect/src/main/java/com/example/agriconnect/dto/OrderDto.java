package com.example.agriconnect.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.agriconnect.enums.OrderStatus;

import lombok.Data;

@Data
public class OrderDto {
	private Long id;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String reason;
    private UserDto user;
    private List<OrderItemDto> orderItems;
    private PaymentDto payment;
    private DeliveryDto  deliveryDto;
    private Double totalAmount;
//       private String deliveryName;
//	private String deliveryMobileNumber;
}
	