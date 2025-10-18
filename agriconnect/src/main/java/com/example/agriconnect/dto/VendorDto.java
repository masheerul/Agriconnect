package com.example.agriconnect.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.agriconnect.entity.LocationEntity;
import com.example.agriconnect.enums.VendorStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VendorDto {
	 private Long id;
	    private String name;
	    private String email;
	    private String mobileNumber;
	    private String shopName;
	    private boolean verified;
	   private String otp;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    private LocationDto location;
	    private VendorStatus status;
	   
}
