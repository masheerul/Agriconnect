package com.example.agriconnect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.agriconnect.entity.AdminEntity;
import com.example.agriconnect.entity.UserEntity;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
		Optional<AdminEntity>findByEmail(String email);
		Optional<AdminEntity> findByMobileNumber(String mobileNumber);
}
