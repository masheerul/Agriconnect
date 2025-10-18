package com.example.agriconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriconnect.dto.DeliveryDto;
import com.example.agriconnect.dto.LocationDto;
import com.example.agriconnect.dto.OrderDto;
import com.example.agriconnect.dto.OrderTrackingDto;
import com.example.agriconnect.service.OrderService;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

		
		@Autowired
		private OrderService service;
		
		  @PostMapping("/place")
		    public ResponseEntity<?> placeOrder(
		            @RequestParam(required = false) Long userId,
		            @RequestParam(required = false) Long productId,
		            @RequestParam(defaultValue = "1") int quantity,
		            @RequestParam(defaultValue = "false") boolean fromCart,
		            @RequestBody(required = false) DeliveryDto deliveryDto
		    ) {
		        if (userId == null) {
		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		                    .body("⚠️ Please login or register to place an order.");
		        }

		        try {
		            OrderDto order = service.placeOrder(userId, productId, quantity, fromCart, deliveryDto);
		            return new ResponseEntity<>(order, HttpStatus.CREATED);
		        } catch (Exception e) {
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                .body("Error placing order: " + e.getMessage());
		        }
		    }
		
		  @GetMapping("/order/user/{userId}")
		    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable Long userId) {
		        List<OrderDto> orders = service.getOrdersByUser(userId);
		        return ResponseEntity.ok(orders);
		    }

		    @GetMapping("/{orderId}/status")
		    public ResponseEntity<OrderTrackingDto> getOrderStatus(@PathVariable Long orderId) {
		        OrderTrackingDto dto = service.getOrderStatus(orderId);
		        return ResponseEntity.ok(dto);
		    }
//		
		@PostMapping("/orders/{orderId}/return/{userId}")
		public ResponseEntity<OrderDto>returnProduct(@PathVariable Long orderId,@PathVariable Long userId, @RequestBody String reason){
			OrderDto dto=service.returnOrder(orderId, userId, reason);
			return new ResponseEntity<>(dto,HttpStatus.CREATED); 
		}
}
