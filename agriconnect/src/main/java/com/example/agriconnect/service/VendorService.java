package com.example.agriconnect.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.agriconnect.dto.VendorDto;
import com.example.agriconnect.entity.LocationEntity;
import com.example.agriconnect.entity.UserEntity;
import com.example.agriconnect.entity.VendorEntity;
import com.example.agriconnect.enums.Role;
import com.example.agriconnect.enums.VendorStatus;
import com.example.agriconnect.exception.VendorNotFoundException;
import com.example.agriconnect.jwtutil.JwtUtil;
import com.example.agriconnect.repository.LocationRepository;
import com.example.agriconnect.repository.UserRepository;
import com.example.agriconnect.repository.VendorRepository;

@Service
public class VendorService {
	
		
	@Autowired
	private VendorRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired 
	private ModelMapper mapper;
	@Autowired 
	private LocationRepository locationRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public VendorDto convertToDto(VendorEntity entity) {
		return mapper.map(entity, VendorDto.class);
	}
	public VendorEntity convertToEntity(VendorDto dto) {
		return mapper.map(dto, VendorEntity.class);
	}
		
	public VendorDto registerVendor(VendorDto dto) {
		
	    if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
	        throw new RuntimeException("Email already registered");
	    }
	    if (userRepository.findByMobileNumber(dto.getMobileNumber()).isPresent()) {
	        throw new RuntimeException("Mobile number already registered");
	    }

	    UserEntity user = new UserEntity();
	    user.setName(dto.getName());
	    user.setEmail(dto.getEmail());
	    user.setMobileNumber(dto.getMobileNumber());
//	    user.setAddress(dto.getLocation() != null ? dto.getLocation().getDistrict() : null);
	    user.setCreatedAt(LocalDateTime.now());
	   
	    user.getRoles().add(Role.USER);
	    user.getRoles().add(Role.VENDOR);
	    UserEntity savedUser = userRepository.save(user);

	    VendorEntity vendor = mapper.map(dto, VendorEntity.class);
	    vendor.setUser(savedUser);
	    vendor.setStatus(VendorStatus.PENDING);
	    vendor.setOtp(generateOtp());
	    vendor.setRole(Role.VENDOR);
	    vendor.setCreatedAt(LocalDateTime.now());

	    if (dto.getLocation() != null) {
	        LocationEntity locationEntity = mapper.map(dto.getLocation(), LocationEntity.class);
	        Optional<LocationEntity> existingLocation = locationRepository
	                .findByStateAndDistrictAndSubLocationAndZipCode(
	                        locationEntity.getState(),
	                        locationEntity.getDistrict(),
	                        locationEntity.getSubLocation(),
	                        locationEntity.getZipCode()
	                );

	        vendor.setLocationEntity(existingLocation.orElseGet(() -> locationRepository.save(locationEntity)));
	    }

	   
	    VendorEntity savedVendor = repository.save(vendor);
//	    sendOtpToVendor(savedVendor);
	    return mapper.map(savedVendor, VendorDto.class);
	}
	private String generateOtp() {
	    Random random = new Random();
	    int otp = 100000 + random.nextInt(900000); 
	    return String.valueOf(otp);
	}
//	

	
	
	public VendorDto updatevendor(Long id , VendorDto updatedVendor) {
		
			VendorEntity existingVendor=repository.findById(id).orElseThrow(()->  new VendorNotFoundException("Vendor not found id "+id));
			if(updatedVendor.getName() !=null) {
				existingVendor.setName(updatedVendor.getName());
			}
			if(updatedVendor.getEmail() != null) {
				existingVendor.setEmail(updatedVendor.getEmail());
			}
			if(updatedVendor.getMobileNumber() != null) {
				existingVendor.setMobileNumber(updatedVendor.getMobileNumber());
				}
				if(updatedVendor.getShopName() != null) {
					existingVendor.setShopName(updatedVendor.getShopName());
				}
				existingVendor.setUpdatedAt(LocalDateTime.now());
				
				VendorEntity entity=repository.save(existingVendor);
				return convertToDto(entity);
//				
			
	}
	
//	
	public VendorDto getVendorById(Long id) {
	    VendorEntity vendor = repository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + id));
	   return mapper.map(vendor,VendorDto.class);  
	}
	
	public VendorEntity loginVendor(String identifier, String otp) {
        Optional<VendorEntity> optionalVendor = repository.findByEmail(identifier);
        if (!optionalVendor.isPresent()) {
            optionalVendor = repository.findByMobileNumber(identifier);
        }

        VendorEntity vendor = optionalVendor.orElseThrow(
            () -> new RuntimeException("Vendor not found with identifier: " + identifier)
        );

        if (vendor.getOtp() == null || !vendor.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // Optionally clear OTP after successful login
//        vendor.setOtp(null);
        repository.save(vendor);
        

        return vendor;
    }
	 public String generateToken(String vendorEmail) {
	        
	        return JwtUtil.generateToken(vendorEmail, "VENDOR"); 
	        
	    }
    
//    public VendorDto getVendorProfilleByEmail(String email) {
//    	VendorEntity entity=repository.findByEmail(email)
//    			.orElseThrow(()-> new VendorNotFoundException("vendor id not found"));
//    	VendorDto dto=mapper.map(entity, VendorDto.class);
//    	return dto;
//    }
}
