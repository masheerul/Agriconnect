package com.example.agriconnect.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ProductDto {
	private Long id;
    private String productName;
   
    private int productQuantity;
    private String category;
    private double cost;
    private String image;        // image filename or URL
    private String description;
    private String unit;
    private transient MultipartFile imageFile;
}
