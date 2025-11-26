package com.example.agriconnect.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.agriconnect.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	 @Column(unique = true)
	private String email;
	 @Column(unique = true)
	private String mobileNumber;
	
	
    private LocalDateTime createdAt;
	private String otp;
	private LocalDateTime otpExpiry;
	private boolean isVerified;
	 @ElementCollection(fetch = FetchType.EAGER)
	    @Enumerated(EnumType.STRING)
	    private Set<Role> roles = new HashSet(); 
	 
	 
	 @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	    private VendorEntity vendorProfile;
	 
	 
	 @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
	 private List<OrderEntity>orderEntities;
	 
	 @OneToOne(mappedBy = "userEntity" , cascade = CascadeType.ALL)
	 private CartEntity cartEntity;
	 
	 @ManyToOne
	    @JoinColumn(name = "location_id")
	    private LocationEntity locationEntity;
	 
}
