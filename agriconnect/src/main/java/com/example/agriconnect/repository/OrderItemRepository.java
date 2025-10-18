package com.example.agriconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agriconnect.entity.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long>{

}
