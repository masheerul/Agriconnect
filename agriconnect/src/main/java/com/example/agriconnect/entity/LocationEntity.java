package com.example.agriconnect.entity;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
	    name = "locations",
	    uniqueConstraints = @UniqueConstraint(
	        columnNames = {"state", "district", "subLocation", "zipCode"}
	    )
	    )
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String state;
    @Column(length = 100)
    private String district;
    @Column(length = 100)
    private String subLocation; 
    @Column(length = 10)
    private String zipCode;

    @OneToMany(mappedBy = "locationEntity", cascade = CascadeType.ALL)
    private List<VendorEntity> vendorEntities;
    @OneToMany(mappedBy = "locationEntity", cascade = CascadeType.ALL)
    private List<UserEntity> userEntities;
}