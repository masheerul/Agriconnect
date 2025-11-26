package com.example.agriconnect.controller;

import java.util.List;
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

import com.example.agriconnect.dto.AdminDto;
import com.example.agriconnect.dto.VendorDto;
import com.example.agriconnect.enums.VendorStatus;
import com.example.agriconnect.service.AdminService;
import com.example.agriconnect.service.TokenBlacklistService;

@RestController
@RequestMapping("api/admin")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdminController {
		
		@Autowired
		private AdminService adminService;
		@Autowired
		private TokenBlacklistService tokenBlacklistService;

		
		@PostMapping("/register")
		public ResponseEntity<AdminDto> registerAdmin(@RequestBody AdminDto dto){
			AdminDto adminDto=adminService.registerAdmin(dto);
			return new ResponseEntity<>(adminDto,HttpStatus.CREATED);
		}
		
		@PostMapping("/login")
		public ResponseEntity<Map<String, String>>loginAdmin(@RequestParam String identifier, @RequestParam String password){
			String token=adminService.loginAdminByOtp(identifier,password);
			
			Map<String, String> response = Map.of(
			        "message", "Login successful!",
			        "token", token
			    );
			 return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		 @GetMapping("/status/{status}")
		    public List<VendorDto> getVendorsByStatus(@PathVariable VendorStatus status) {
		        return adminService.getByStatus(status);
		    }

		    @PutMapping("/approve/{vendorId}")
		    public VendorDto approveVendor(@PathVariable Long vendorId) {
		        return adminService.approveVendor(vendorId);
		    }

		    @PutMapping("/reject/{vendorId}")
		    public VendorDto rejectVendor(@PathVariable Long vendorId) {
		        return adminService.rejectVendor(vendorId);
		    }
		    @PostMapping("/logout")
		    public ResponseEntity<Map<String, String>> logoutAdmin(@RequestHeader("Authorization") String token) {
		        tokenBlacklistService.logout(token);
		        return ResponseEntity.ok(Map.of("message", "Admin logged out successfully"));
		    }
}
