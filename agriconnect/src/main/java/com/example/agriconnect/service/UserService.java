package com.example.agriconnect.service;

import java.security.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.agriconnect.dto.LocationDto;
import com.example.agriconnect.dto.UserDto;
import com.example.agriconnect.entity.LocationEntity;
import com.example.agriconnect.entity.UserEntity;
import com.example.agriconnect.enums.Role;
import com.example.agriconnect.exception.InvalidOtpException;
import com.example.agriconnect.exception.UserNotFoundException;
import com.example.agriconnect.jwtutil.JwtUtil;
import com.example.agriconnect.repository.LocationRepository;
import com.example.agriconnect.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

			@Autowired
			private UserRepository repository;
			
			@Autowired
			private LocationRepository locationRepository;
			@Autowired
			private ModelMapper mapper;
			@Autowired
			private SmsService smsService;
			@Autowired
			private EmailService emailService;
			
			public UserDto convertToDto(UserEntity user) {
		        return mapper.map(user, UserDto.class);
		    }

		    public UserEntity convertToEntity(UserDto userDTO) {
		        return mapper.map(userDTO, UserEntity.class);
		    }
		    
		    
		    public String generateOtp() {
		        Random random = new Random();
		        int otp = random.nextInt(900000) + 100000;
		        return String.valueOf(otp);
		    }
		    
		    public UserDto registerUser(UserDto dto){
		    	Optional<UserEntity> existingUser = repository.findByEmail(dto.getEmail());
		            if (existingUser.isPresent()) {
		                throw new UserNotFoundException("Email already registered .");
		            }

		            Optional<UserEntity> existingUserByMobile = repository.findByMobileNumber(dto.getMobileNumber());
		            if (existingUserByMobile.isPresent()) {
		                throw new UserNotFoundException("Mobile number already registered");
		            }
		            
		            UserEntity entity=mapper.map(dto, UserEntity.class);
		            entity.setCreatedAt(LocalDateTime.now());
		           entity.setRoles(Set.of(Role.USER));
		            
		           String otp = generateOtp();
		           entity.setOtp(otp);
		           
		            if (dto.getLocation() != null) {
		    	        LocationEntity locationEntity = mapper.map(dto.getLocation(), LocationEntity.class);

		    	       
		    	        Optional<LocationEntity> existingLocation = locationRepository
		    	                .findByStateAndDistrictAndSubLocationAndZipCode(
		    	                        locationEntity.getState(),
		    	                        locationEntity.getDistrict(),
		    	                        locationEntity.getSubLocation(),
		    	                        locationEntity.getZipCode()
		    	                );

		    	     
		    	        entity.setLocationEntity(existingLocation.orElseGet(() -> locationRepository.save(locationEntity)));
		    	    }
		            
		            
		            
		            UserEntity savedEntity = repository.save(entity);
		            smsService.sendOtpSms(savedEntity.getMobileNumber(),otp);
		            emailService.sendotpEMail(savedEntity.getEmail(),otp);
		            return mapper.map(savedEntity, UserDto.class);
		            
		    		
}
		    
		    	public String loginByOtp(String identifier, String otp){
		    		Optional<UserEntity>otpUser=repository.findByEmail(identifier);
		    		if(!otpUser.isPresent()) {
		    			otpUser=repository.findByMobileNumber(identifier);
		    		}
		    		 if (!otpUser.isPresent()) {
		    		        throw new UserNotFoundException("User not found with identifier: " + identifier);
		    		    }
		    		
		    		UserEntity user=otpUser.get();
		    		if(user.getOtp() == null || !user.getOtp().equals(otp)) {
		    			throw new InvalidOtpException("Invalid Exception");
		    		}
		    		
//		    		user.setOtp(null);
//		    		repository.save(user);
//		    		
		    		// Get all role names as a comma-separated string or choose one
		    		String roleNames = user.getRoles().stream()
		    		    .map(Enum::name)
		    		    .findFirst()
		    		    .orElse("USER"); 

		    		String token = JwtUtil.generateToken(String.valueOf(user.getId()), roleNames);
		    		return  token;
		    	}
		    	
		    	
		    	public UserDto getUserProfile(Long userId) {
		          
		            UserEntity userEntity = repository.findById(userId)
		                    .orElseThrow(() -> new RuntimeException("User not found"));

		            UserDto userDto = mapper.map(userEntity, UserDto.class);
		            return userDto;
		        }
		    
		    	public UserDto updateOwnProfile(Long id , UserDto updatedUser){
		    		UserEntity existingUser=repository.findById(id)
		    				.orElseThrow(()-> new UserNotFoundException("User not find with id"+ id));
		    			
		    		if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
		    	        if(repository.findByEmail(updatedUser.getEmail()).isPresent()) {
		    	            throw new RuntimeException("Email is already taken");
		    	        }
		    	        existingUser.setEmail(updatedUser.getEmail());
		    	    }
		    		
		    		if(updatedUser.getName() !=null) {
		    			existingUser.setName(updatedUser.getName());
		    		}
		    		
		    		if(updatedUser.getEmail() != null) {
		    			existingUser.setEmail(updatedUser.getEmail());
		    		}
		    		
		    		UserEntity savedUser = repository.save(existingUser);

		    	    return convertToDto(savedUser);
		    		
		    	}
		    	
//		   
}

