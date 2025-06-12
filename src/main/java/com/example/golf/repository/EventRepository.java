package com.example.golf.repository;

import com.example.golf.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, String> {

    @Query("SELECT e FROM Event e WHERE e.isDeleted = false")
    List<Event> findByDeletedFalse();

    @Query("SELECT e FROM Event e WHERE e.type = :type AND :date between e.startDate and e.endDate AND e.isDeleted = false and e.status = :status and (e.targetUserType = :role or e.targetUserType = 'ALL')")
    List<Event> findEventForUser(
            @Param("type") String type,
            @Param("date") LocalDate date,
            @Param("status") String status,
            @Param("role") String role
    );
    @Query("SELECT e FROM Event e WHERE e.type = 'PROMOTION' AND e.isDeleted = false " +
            "AND e.status = 'ACTIVE' AND (e.targetUserType = :role OR e.targetUserType = 'ALL') " +
            "ORDER BY e.discountPercent DESC")
    Event findPromotionEventForUser(String role);
}
