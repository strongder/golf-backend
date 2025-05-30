package com.example.golf.repository;

import com.example.golf.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, String> {
    Optional<Guest> findGuestByFullNameAndPhone(String fullName, String phone);

    Optional<Guest> findByUserId(String userId);
}
