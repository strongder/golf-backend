package com.example.golf.repository;

import com.example.golf.enums.ServiceType;
import com.example.golf.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServicesRepository extends JpaRepository<Services, String> {
    @Query("SELECT s.price FROM Services s WHERE s.name = ?1")
    String getPriceByService(String serviceName);

    @Query("SELECT s FROM Services s WHERE s.isDeleted = false AND s.type = :type")
    List<Services> findServicesByType(ServiceType type);

    @Query("SELECT s FROM Services s WHERE s.isDeleted = false AND s.type != :type")
    List<Services> findServicesByTypeNot(ServiceType type);

    @Query("SELECT s FROM Services s WHERE s.isDeleted = false")
    List<Services> findServicesIsDeleteFalse();
}
