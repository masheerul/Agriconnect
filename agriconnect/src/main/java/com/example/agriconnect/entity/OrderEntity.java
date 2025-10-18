package com.example.agriconnect.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.example.agriconnect.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	 private String deliveryName;
	    private String deliveryMobileNumber;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	 @Enumerated(EnumType.STRING)  
	    private OrderStatus status;
	private Double totalAmount;
	private String reason;
	@ManyToOne
	@JoinColumn(name = "user_Id")
	private UserEntity user;
	
	@OneToMany(mappedBy = "orderEntity" , cascade = CascadeType.ALL)
	private List<OrderItemEntity> orderItems;
	
	@ManyToOne
    @JoinColumn(name = "delivery_location_id")
    private LocationEntity deliveryLocation;
	
	@OneToOne(mappedBy = "orderEntity" , cascade = CascadeType.ALL)
	private PaymentEntity paymentEntity;	
}
