package com.example.agriconnect.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartDto {
		
	private Long id;
    private UserDto user;
    private List<CartItemDto> cartItems;
    private double totalAmount;
    private int totalItems;	
}
