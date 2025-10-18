package com.example.agriconnect.dto;

import lombok.Data;

@Data
public class CartItemDto {
	
	 private Long id;
	    private ProductDto product;
	    private int quantity;
	    private double price;
}
