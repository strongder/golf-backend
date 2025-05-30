package com.example.golf.repository;

import com.example.golf.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServicesRepository extends JpaRepository<Services, String> {
    @Query("SELECT s.price FROM Services s WHERE s.name = ?1")
    String getPriceByService(String serviceName);
}
