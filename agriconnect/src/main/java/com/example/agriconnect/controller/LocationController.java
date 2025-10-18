package com.example.agriconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriconnect.dto.LocationDto;
import com.example.agriconnect.service.LocationServcie;

@RestController
@RequestMapping("api")
public class LocationController {

	
		@Autowired
		private LocationServcie servcie;
		
		@PostMapping("/savelocation")
		public ResponseEntity<LocationDto> saveLocation(LocationDto dto){
			LocationDto locationDto=servcie.saveLocation(dto);
			return new ResponseEntity<>(locationDto,HttpStatus.CREATED);
		}
		
//		@GetMapping("/states")
//	    public ResponseEntity<List<String>> getAllStates() {
//	        return ResponseEntity.ok(servcie.findAllStates());
//	    }
		
//		@GetMapping("/state")
//		public ResponseEntity<List<LocationDto>>getByState(@RequestParam("name") String state){
//			List<LocationDto> dto=servcie.findByState(state);
//			return new ResponseEntity<>(dto,HttpStatus.OK);
//		}
		@GetMapping("/district")
		public ResponseEntity<List<LocationDto>> findByDistrict(@RequestParam("name") String district ){
			List<LocationDto> list=servcie.findByDistrict(district);
			return new ResponseEntity<>(list,HttpStatus.OK);
		}
}
