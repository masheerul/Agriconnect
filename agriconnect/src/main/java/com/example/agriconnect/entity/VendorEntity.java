package com.example.agriconnect.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.agriconnect.enums.Role;
import com.example.agriconnect.enums.VendorStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vendors")
public class VendorEntity {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		private String name;
		@Column(unique = true)
		private String email;
		@Column(unique = true)
		private String mobileNumber;
		private String shopName;
		@Column(name = "is_verified")
	    private boolean isVerified;
	      private String otp;
	    @Enumerated(EnumType.STRING)
	    private VendorStatus status;
	    
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private Role role; 
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
		
	    @OneToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private UserEntity user;
	    
	    
		@ManyToOne
		@JoinColumn(name = "location_id")
		private LocationEntity locationEntity;
		
		@OneToMany(mappedBy = "vendorEntity",cascade = CascadeType.ALL)
		private List<ProductEntity>productEntities;
		
	
}
