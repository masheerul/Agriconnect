package com.example.agriconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agriconnect.entity.CartItemEntity;
import com.example.agriconnect.entity.UserEntity;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long>{
	
	List<CartItemEntity> findByCart_UserEntity(UserEntity user);
}
