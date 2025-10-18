package com.example.agriconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agriconnect.entity.OrderEntity;

public interface OrderRepository extends  JpaRepository<OrderEntity, Long>{
	List<OrderEntity> findByUser_Id(Long userId);
}
