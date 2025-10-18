package com.example.agriconnect.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.agriconnect.dto.AdminDto;
import com.example.agriconnect.dto.VendorDto;
import com.example.agriconnect.entity.AdminEntity;
import com.example.agriconnect.entity.VendorEntity;
import com.example.agriconnect.enums.Role;
import com.example.agriconnect.enums.VendorStatus;
import com.example.agriconnect.exception.VendorNotFoundException;
import com.example.agriconnect.jwtutil.JwtUtil;
import com.example.agriconnect.repository.AdminRepository;
import com.example.agriconnect.repository.VendorRepository;

@Service
public class AdminService {

		
		@Autowired
		private AdminRepository adminRepository;
		@Autowired
		private VendorRepository vendorRepository;
		@Autowired
		private ModelMapper mapper;
		
		public AdminDto registerAdmin(AdminDto adminDto) {
	        if (adminRepository.findByEmail(adminDto.getEmail()).isPresent()) {
	            throw new RuntimeException("Admin with email already exists");
	        }

	        AdminEntity admin = mapper.map(adminDto, AdminEntity.class);
	        admin.setRole(Role.ADMIN);
	        AdminEntity savedAdmin = adminRepository.save(admin);

	        return mapper.map(savedAdmin, AdminDto.class);
		
}
		public String loginAdminByOtp(String identifier, String password) {
		    Optional<AdminEntity> otpAdmin = adminRepository.findByEmail(identifier);
		    if (!otpAdmin.isPresent()) {
		        otpAdmin = adminRepository.findByMobileNumber(identifier);
		    }
		    if (!otpAdmin.isPresent()) {
		        throw new RuntimeException("Admin not found with identifier: " + identifier);
		    }

		    AdminEntity admin = otpAdmin.get();
		    if (admin.getPassword() == null || !admin.getPassword().equals(password)) {
		        throw new RuntimeException("Invalid OTP");
		    }

		    // Optional: clear OTP
		    // admin.setOtp(null);
		    // adminRepository.save(admin);

		    String roleName = admin.getRole().name();
		    String token = JwtUtil.generateToken(String.valueOf(admin.getId()), roleName);
		    return token;
		}

		 
		 public VendorDto approveVendor(Long vendorId) {
			 VendorEntity entity=vendorRepository.findById(vendorId)
					 .orElseThrow(()-> new VendorNotFoundException("vendor not found"));
			 entity.setStatus(VendorStatus.APPROVED);
			 	VendorEntity savedStatus=vendorRepository.save(entity);
			 	return mapper.map(savedStatus, VendorDto.class);
		 }
		 
		 public VendorDto rejectVendor(Long vendorId) {
			 VendorEntity entity=vendorRepository.findById(vendorId)
					 .orElseThrow(()-> new VendorNotFoundException("vendor not found"));
			 
			 entity.setStatus(VendorStatus.REJECTED);
			 VendorEntity savedStatus=vendorRepository.save(entity);
			 return mapper.map(savedStatus, VendorDto.class);
		 }
		 
		 public List<VendorDto> getByStatus(VendorStatus status){
			 List<VendorEntity>entities=vendorRepository.findByStatus(status);
					 return entities.stream()
							 .map(v ->mapper.map(v, VendorDto.class))
							 .collect(Collectors.toList());
		 }
		 
		 
		 
		 
}