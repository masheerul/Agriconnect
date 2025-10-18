package com.example.agriconnect.controller;

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

import com.example.agriconnect.dto.UserDto;
import com.example.agriconnect.entity.UserEntity;
import com.example.agriconnect.jwtutil.JwtUtil;
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
	 @PostMapping("/registerUser")
	 public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserDto dto) {
	     UserDto savedUser = service.registerUser(dto);
	     String token = service.loginByOtp(savedUser.getEmail(), savedUser.getOtp());

	     Map<String, String> response = Map.of(
	         "message", "User registered successfully!",
	         "token", token,
	         "identifier", savedUser.getEmail(),
	         "userId", savedUser.getId().toString()
	     );

	     return ResponseEntity.status(HttpStatus.CREATED).body(response);
	 }


	@PutMapping("/users/{id}/update")
	public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto entity){
		UserDto updated=service.updateOwnProfile(id, entity);
		return new ResponseEntity<>(updated, HttpStatus.OK);	
		}
	
	@PostMapping("/auth/login")
	public ResponseEntity<Map<String, String>> login(@RequestParam String identifier, @RequestParam String otp){
	    String token = service.loginByOtp(identifier, otp);

	    Map<String, String> response = Map.of(
	        "message", "Login successful!",
	        "token", token
	    );

	    return ResponseEntity.ok(response);
	}
	@GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String token) {
        String userId = jwtUtil.getUserIdFromToken(token.substring(7)); 
        UserDto userProfile = service.getUserProfile(Long.parseLong(userId));

        return ResponseEntity.ok(userProfile);
    }
	
//	@PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        return ResponseEntity.ok(service.logout(request, response, authentication));
//    }
			
}
