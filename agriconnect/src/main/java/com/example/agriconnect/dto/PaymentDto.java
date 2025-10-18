package com.example.agriconnect.dto;

import com.example.agriconnect.enums.PaymentMethods;
import com.example.agriconnect.enums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentDto {
		
	
	private Long id;
    private String transactionId;
    private Double totalAmount;
    private PaymentMethods paymentMethod;
    private PaymentStatus status;
}
