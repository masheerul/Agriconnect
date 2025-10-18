package com.example.agriconnect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.agriconnect.entity.PaymentEntity;

public interface PaymentRepository  extends JpaRepository<PaymentEntity, Long>{

	 Optional<PaymentEntity> findByTransactionId(String transactionId);
	 
}
