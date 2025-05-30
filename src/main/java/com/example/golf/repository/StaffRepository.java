package com.example.golf.repository;

import com.example.golf.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, String> {
    Optional<Staff> findByPhone(String phone);
    Optional<Staff> findStaffByFullNameAndPhone(String fullName, String phone);

    @Query("SELECT COUNT(s) FROM Staff s")
    int countStaff();

    Optional<Staff> findByUserId(String id);
}
