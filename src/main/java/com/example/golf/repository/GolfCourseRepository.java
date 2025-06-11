package com.example.golf.repository;

import com.example.golf.model.GolfCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GolfCourseRepository extends JpaRepository<GolfCourse, String> {
    @Query("SELECT g FROM GolfCourse g WHERE g.isDeleted = false")
    List<GolfCourse> findByIsDeletedFalse();
}
