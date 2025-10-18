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

import com.example.agriconnect.dto.VendorDto;
import com.example.agriconnect.entity.VendorEntity;
import com.example.agriconnect.enums.VendorStatus;
import com.example.agriconnect.jwtutil.JwtUtil;
import com.example.agriconnect.service.VendorService;

import io.jsonwebtoken.lang.Collections;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:3000")
public class VendorController {

	
	@Autowired
	private VendorService vendorService;
	@Autowired 
	private JwtUtil jwtUtil;
	
	@PostMapping("/vendor/register")
	public ResponseEntity<Map<String, String>> registerVendor(@RequestBody VendorDto dto) {
	    VendorDto savedVendor = vendorService.registerVendor(dto);
	    String token = vendorService.generateToken(savedVendor.getEmail());

	    Map<String, String> response = Map.of(
	        "message", "Vendor registered successfully!",
	        "token", token,
	        "identifier", savedVendor.getEmail(),
	        "vendorId", savedVendor.getId().toString()
	    );

	    return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	
//	 @PutMapping("/vendor/{id}/status")
//	    public ResponseEntity<VendorDto> updateVendorStatus(
//	            @PathVariable Long id,
//	            @RequestParam VendorStatus status) {
//
//	        VendorDto dto = adminService.changeVendorStatus(id, status);
//	        return ResponseEntity.ok(dto);
	
			// this is also inside admin 
//	    }
	@PostMapping("/vendor/login")
	 public ResponseEntity<Map<String, String>> loginVendor(
	         @RequestParam String identifier, 
	         @RequestParam String otp) {

	     VendorEntity vendor = vendorService.loginVendor(identifier, otp);
	     String token = vendorService.generateToken(vendor.getEmail());

	     Map<String, String> response = Map.of(
	         "message", "Login successful",
	         "token", token,
	         "vendorId", vendor.getId().toString()
	     );

	     return ResponseEntity.ok(response);
	 }

	 
	 @GetMapping("/vendor/{id}")
	 public ResponseEntity<VendorDto> getVendorById(@PathVariable Long id) {
	     VendorDto vendor = vendorService.getVendorById(id);
	     if (vendor != null) {
	         return ResponseEntity.ok(vendor);
	     } else {
	         return ResponseEntity.notFound().build();
	     }
	 }
	 
	 @PutMapping("/vendor/update")
	 public ResponseEntity<VendorDto>updateProfile(@PathVariable Long id, @RequestBody VendorDto dto){
		 VendorDto vendorDto=vendorService.updatevendor(id, dto);
		 return new ResponseEntity<>(vendorDto,HttpStatus.OK);
	 }
	 
//	 @GetMapping("/vendor/profile")
//	 public ResponseEntity<VendorDto> getProfile(@RequestHeader("Authorization") String token) {
//	     String email = jwtUtil.getVendorIdFromToken(token.substring(7)); // <- returns email
//	     VendorDto dto = vendorService.getVendorProfilleByEmail(email);
//	     return ResponseEntity.ok(dto);
//	 }

}

