package com.example.agriconnect.dto;

import java.time.LocalDateTime;

import com.example.agriconnect.enums.Role;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@NoArgsConstructor
public class UserDto {
	private Long id;
	private String name;
	private String email;
	private String mobileNumber;
	private String otp;
	private LocationDto location;
}
