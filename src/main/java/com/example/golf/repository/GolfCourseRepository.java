package com.example.golf.repository;

import com.example.golf.model.GolfCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GolfCourseRepository extends JpaRepository<GolfCourse, String> {
}
