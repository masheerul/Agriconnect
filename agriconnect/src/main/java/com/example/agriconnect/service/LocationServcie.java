package com.example.agriconnect.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.agriconnect.dto.LocationDto;
import com.example.agriconnect.entity.LocationEntity;
import com.example.agriconnect.repository.LocationRepository;

@Service
public class LocationServcie {

	
				@Autowired 
				private LocationRepository repository;
				
				@Autowired
				private ModelMapper mapper;
				
				public LocationDto convertToDto(LocationEntity entity) {
					return mapper.map(entity, LocationDto.class);
				}
				public LocationEntity convertToEntity(LocationDto dto) {
					return mapper.map(dto, LocationEntity.class);
				}
				
				public LocationDto saveLocation(LocationDto dto) {
					LocationEntity entity=convertToEntity(dto);
					LocationEntity savedEntity=repository.save(entity);
					return convertToDto(savedEntity);
				}
				
//			
//				public List<LocationDto> findByState(String state){
//					return repository.findByState(state).stream()
//							.map(entity -> mapper.map(entity, LocationDto.class))
//							.collect(Collectors.toList());
//				}
				
				public List<LocationDto> findByDistrict(String district){
					return repository.findByDistrict(district).stream()
							.map(entity -> mapper.map(entity, LocationDto.class))
							.collect(Collectors.toList());
				}
}
