package com.example.agriconnect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agriconnect.entity.CartEntity;
import com.example.agriconnect.entity.ProductEntity;
import com.example.agriconnect.entity.UserEntity;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
	
	Optional<CartEntity> findByUserEntity(UserEntity user);

	
	
}
