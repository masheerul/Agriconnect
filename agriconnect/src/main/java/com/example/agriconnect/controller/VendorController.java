package com.example.agriconnect.controller;

import java.io.Serial;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriconnect.dto.LoginResponse;
import com.example.agriconnect.dto.VendorDto;
import com.example.agriconnect.entity.VendorEntity;
import com.example.agriconnect.enums.VendorStatus;
import com.example.agriconnect.jwtutil.JwtUtil;
import com.example.agriconnect.service.TokenBlacklistService;
import com.example.agriconnect.service.VendorService;

import io.jsonwebtoken.lang.Collections;

@RestController
@RequestMapping("api/vendor")
@CrossOrigin(origins = "http://localhost:3000")
public class VendorController {

	
	@Autowired
	private VendorService vendorService;
	@Autowired 
	private JwtUtil jwtUtil;
	@Autowired
	private TokenBlacklistService tokenBlacklistService;
	
	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> registerVendor(@RequestBody VendorDto dto) {
	    VendorDto savedVendor = vendorService.registerVendor(dto);
	    Map<String, String> response = Map.of(
	        "message", "Vendor registered successfully!",
	        "identifier", savedVendor.getEmail(),
	        "vendorId", savedVendor.getId().toString()
	    );

	    return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> loginVendor(
	        @RequestParam String identifier,
	        @RequestParam String otp) {

	    String token = vendorService.loginVendor(identifier, otp);

	    VendorEntity vendor = vendorService.getVendorByIdentifier(identifier);

	    LoginResponse loginResponse = new LoginResponse(
	            vendor.getId(),
	            vendor.getEmail(),
	            "ROLE_VENDOR",
	            token,
	            vendor.isVerified(),
	            vendor.getId()
	    );

	    return ResponseEntity.ok(loginResponse);
	}
	 
	 @GetMapping("/{id}")
	 public ResponseEntity<VendorDto> getVendorById(@PathVariable Long id) {
	     VendorDto vendor = vendorService.getVendorById(id);
	     if (vendor != null) {
	         return ResponseEntity.ok(vendor);
	     } else {
	         return ResponseEntity.notFound().build();
	     }
	 }
	 
	 @PutMapping("/update/{id}")
	 public ResponseEntity<VendorDto>updateProfile(@PathVariable Long id, @RequestBody VendorDto dto){
		 VendorDto vendorDto=vendorService.updatevendor(id, dto);
		 return new ResponseEntity<>(vendorDto,HttpStatus.OK);
	 }
	 @PostMapping("/logout")
	 public ResponseEntity<Map<String, String>> logoutVendor(@RequestHeader("Authorization") String token) {
	     tokenBlacklistService.logout(token);
	     return ResponseEntity.ok(Map.of("message", "Vendor logged out successfully"));
	 }
	 
	 @PostMapping("/send-otp")
	 public ResponseEntity<String> sendOtp(
	         @RequestParam String identifier) { 
		 String otp=vendorService.sendOtp(identifier);
			return new ResponseEntity<>(otp,HttpStatus.OK);
			
	 }
	 @PostMapping("/verify-otp")
	 public ResponseEntity<String> verifyOtp(
	         @RequestParam String identifier,
	         @RequestParam String otp) {

	     String message = vendorService.verifyOtp(identifier, otp);
	     return ResponseEntity.ok(message);
	 }


}

