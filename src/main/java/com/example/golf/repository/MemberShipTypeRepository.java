package com.example.golf.repository;

import com.example.golf.enums.MembershipStatus;
import com.example.golf.model.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberShipTypeRepository extends JpaRepository<MembershipType, String> {
    @Query("SELECT m FROM MembershipType m WHERE m.isDeleted = false")
    List<MembershipType> findAllByDeletedFalse();

    @Query("SELECT m FROM MembershipType m WHERE m.name = ?1 AND m.isDeleted = false")
    MembershipType findByDeletedIsFalse(String name);

}
