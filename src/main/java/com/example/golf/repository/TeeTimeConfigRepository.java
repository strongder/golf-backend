package com.example.golf.repository;

import com.example.golf.model.TeeTimeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeeTimeConfigRepository extends JpaRepository<TeeTimeConfig, String> {
    @Query("SELECT t FROM TeeTimeConfig t WHERE t.dateType = ?1 AND t.isDeleted = false")
    List<TeeTimeConfig> findByDateType(String dayType); // WEEKDAY hoặc WEEKEND

    @Query("SELECT t FROM TeeTimeConfig t WHERE t.isDeleted = false")
    List<TeeTimeConfig> findByDeletedFalse();

    @Query("SELECT t FROM TeeTimeConfig t WHERE t.golfCourseId = ?1 AND t.dateType = ?2 and t.isDeleted = false")
    TeeTimeConfig findTeeTimeConfigByGolfCourseIdAndDateType(String golfCourseId, String dateType);
}
