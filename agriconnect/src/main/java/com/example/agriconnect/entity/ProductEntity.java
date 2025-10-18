package com.example.agriconnect.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "products")
public class ProductEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String productName; 
	 
	private int productQuantity;
	private String category;
    private String description;
	private double cost;
	private String unit;
	
	@Lob
	@Column(columnDefinition = "LONGTEXT")
	private String image;
	private int stockQuantity;
	@ManyToOne
	@JoinColumn(name = "vendor_id")
	private VendorEntity  vendorEntity;
	
//	@ManyToMany(mappedBy = "productEntities")
//	private List<CartEntity> cartEntities;
	
	@OneToMany(mappedBy = "product" , cascade = CascadeType.ALL)
	private List<OrderItemEntity>orderItemEntities;
	
}
