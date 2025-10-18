  package com.example.agriconnect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.agriconnect.entity.VendorEntity;
import com.example.agriconnect.enums.VendorStatus;

public interface VendorRepository extends JpaRepository<VendorEntity, Long> {
		
	Optional<VendorEntity>findByEmail(String email);
	Optional<VendorEntity> findByMobileNumber(String mobileNumber);
	List<VendorEntity> findByStatus(VendorStatus status);}
