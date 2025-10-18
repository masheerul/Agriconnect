package com.example.agriconnect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agriconnect.dto.LocationDto;
import com.example.agriconnect.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Long>{

	Optional<LocationEntity> findByStateAndDistrictAndSubLocationAndZipCode(
		    String state,
		    String district,
		    String subLocation,
		    String zipCode
		);
	List<LocationEntity>findByState(String state);
	List<LocationEntity> findByDistrict(String District);
}
