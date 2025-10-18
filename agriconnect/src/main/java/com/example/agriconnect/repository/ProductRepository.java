package com.example.agriconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agriconnect.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
	
//	List<ProductEntity> findByVendorEntity_LocationEntity_DistrictContainingIgnoreCase(String district);
//	List<ProductEntity> findByProductNameContainingIgnoreCase(String name);
	List<ProductEntity> findByProductNameContainingIgnoreCaseOrVendorEntity_LocationEntity_DistrictContainingIgnoreCase(
            String name, String district);
}
