
package com.example.agriconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriconnect.dto.PaymentDto;
import com.example.agriconnect.enums.PaymentMethods;
import com.example.agriconnect.service.PaymentService;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	@Autowired
	private PaymentService service;
	
	@PostMapping("/create/order/{orderId}")
	public ResponseEntity<PaymentDto> createPayment(
			@PathVariable Long orderId,
			@RequestParam Double amount,
			@RequestParam PaymentMethods methods,
			 @RequestParam boolean success){
		PaymentDto dto;
		try {
			dto = service.creatPayment(orderId, amount, methods, success);
			System.out.println("Payment Created: " + dto.getTransactionId());

			return new ResponseEntity<>(dto,HttpStatus.CREATED);
		} catch (RazorpayException e) {
			
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	@PostMapping("/update/{transactionId}")
	public ResponseEntity<PaymentDto> updatePaymentStatus(
	        @PathVariable String transactionId,
	        @RequestParam boolean success) {
	    PaymentDto dto = service.updatePaymentStatus(transactionId, success);
	    return ResponseEntity.ok(dto);
	}
	
}
