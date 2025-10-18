package com.example.agriconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrackingDto {
	
	private Long orderId;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private String transactionId;
    private Double totalAmount;
}
