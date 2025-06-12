package com.example.golf.repository;

import com.example.golf.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Query("SELECT n FROM Notification n WHERE n.userId = ?1 AND n.isDeleted = false")
    List<Notification> findByUserId(String userId);

    @Query("SELECT count(n) FROM Notification n WHERE n.userId = ?1 AND n.isRead = false AND n.isDeleted = false")
    int countUnreadNotifications(String userId);
}
