package com.example.agriconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
		
		private String deliveryName;
		private String deliveryMobileNumber;
		private String state;
	    private String district;
	    private String subLocation;
	    private String zipCode;
}
