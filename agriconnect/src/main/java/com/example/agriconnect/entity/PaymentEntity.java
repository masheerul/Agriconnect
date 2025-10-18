package com.example.agriconnect.entity;

import java.util.List;

import com.example.agriconnect.enums.PaymentMethods;
import com.example.agriconnect.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "payment")
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String transactionId;
	 private Double totalAmount;
	
	
	 @Enumerated(EnumType.STRING)
	 @Column(length = 50)
	 private PaymentMethods paymentMethods;
	 @Enumerated(EnumType.STRING)
	 @Column(length = 50)
	 private PaymentStatus status=PaymentStatus.PENDING;
	 
	@OneToOne
	@JoinColumn(name = "order_id")
	private OrderEntity orderEntity;
}
