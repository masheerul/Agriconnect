package com.example.agriconnect.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriconnect.dto.LoginResponse;
import com.example.agriconnect.dto.UserDto;
import com.example.agriconnect.entity.UserEntity;
import com.example.agriconnect.jwtutil.JwtUtil;
import com.example.agriconnect.service.TokenBlacklistService;
import com.example.agriconnect.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {
	
	@Autowired 
	private UserService service;
	 @Autowired
	    private JwtUtil jwtUtil;
	 @Autowired
	 private TokenBlacklistService tokenBlacklistService;
	 
	 
	 @PostMapping("/register")
	 public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserDto userDto) {
	     UserDto savedUser = service.registerUser(userDto);

	     Map<String, String> response = new HashMap<>();
	     response.put("message", "User registered successfully. OTP sent to your mobile and email for verification.");
	     response.put("email", savedUser.getEmail());
	     response.put("mobileNumber", savedUser.getMobileNumber());

	     return ResponseEntity.status(HttpStatus.CREATED).body(response);
	 }


	@PutMapping("/users/{id}/update")
	public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto entity){
		UserDto updated=service.updateOwnProfile(id, entity);
		return new ResponseEntity<>(updated, HttpStatus.OK);	
		}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestParam String identifier,
	                                           @RequestParam String otp) {

	    String token = service.loginByOtp(identifier, otp);

	    UserEntity user = service.getUserByIdentifier(identifier);

	    String role = user.getRoles()
	            .stream()
	            .map(Enum::name)
	            .findFirst()
	            .orElse("USER");

	    LoginResponse loginResponse = new LoginResponse(
	            user.getId(),
	            user.getEmail(),        
	            role,                  
	            token,
	            user.isVerified(),
	    		null
	    		);

	    return ResponseEntity.ok(loginResponse);
	}


	@GetMapping("/profile")
	public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String token) {
	    String username = jwtUtil.extractUsername(token.substring(7));  
	    UserDto userProfile = service.getUserProfileByUsername(username);

	    return ResponseEntity.ok(userProfile);
	}

	
	@PostMapping("/logout")
	public ResponseEntity<Map<String, String>> logoutUser(@RequestHeader("Authorization") String token) {
	    tokenBlacklistService.logout(token);
	    return ResponseEntity.ok(Map.of("message", "User logged out successfully"));
	}
	
	@PostMapping("/send-otp")
	public ResponseEntity<String>sendOtp(@RequestParam String identifier){
		String sendOtp=service.sendOtp(identifier);
		return ResponseEntity.ok(sendOtp);
	}
	@PostMapping("/verify-otp")
	public ResponseEntity<String> verifyOtp(@RequestParam String identifier, @RequestParam String otp){
		String message=service.verifyOtp(identifier, otp);
		return ResponseEntity.ok(message);
	}
			
}
