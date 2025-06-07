package com.example.golf.repository;

import com.example.golf.enums.MembershipStatus;
import com.example.golf.model.Membership;
import com.example.golf.model.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberShipRepository extends JpaRepository<Membership, String> {
    List<Membership> findByUserId(String userId);

    @Query("SELECT m FROM  Membership m  WHERE m.userId = ?1 AND m.isDeleted = false")
    List<Membership> findByUser(String userId);

    @Query("SELECT m FROM Membership m  WHERE m.userId = ?1 AND m.isDeleted = false and m.status = ?2")
    Membership findByUserIdAndStatus(String userId, MembershipStatus status);



}
