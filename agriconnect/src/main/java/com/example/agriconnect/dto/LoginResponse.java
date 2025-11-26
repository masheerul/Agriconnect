package com.example.agriconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long id;              // User or Vendor actual ID
    private String identifier;    // email or mobile
    private String role;          // USER, ADMIN, VENDOR
    private String token;         // JWT token
    private Boolean isVerified;   // true / false
    private Long vendorId;        // only for vendor (else null)
}
