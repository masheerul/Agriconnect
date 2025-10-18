package com.example.agriconnect.dto;

import lombok.Data;

@Data
public class LocationDto {
		
	 private Long id;         
	    private String state;
	    private String district;
	    private String subLocation;
	    private String zipCode;
}
