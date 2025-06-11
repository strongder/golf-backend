package com.example.golf.repository;

import com.example.golf.enums.TeeTimeStatus;
import com.example.golf.model.TeeTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TeeTimeRepository extends JpaRepository<TeeTime, String> {
    boolean existsByDate(LocalDate date);




    @Query("SELECT t FROM TeeTime t WHERE t.golfCourseId = :golfCourseId AND t.date = :date AND" +
            " t.startTime = :startTime AND t.status = :status and t.isDeleted = false ORDER BY t.startTime ASC")
    Optional<TeeTime> findTeeTimeNext(@Param("golfCourseId") String golfCourseId,
                                      @Param("date") LocalDate date,
                                      @Param("startTime") LocalTime startTime,
                                      @Param("status") TeeTimeStatus status);

    @Query("SELECT t FROM TeeTime t WHERE t.golfCourseId = :golfCourseId " +
            "AND t.date = :date AND t.status = :status And t.isDeleted = false ORDER BY t.startTime ASC")
    List<TeeTime> findTeeTimesByDateAndStatus(String golfCourseId,
                                              LocalDate date,
                                              TeeTimeStatus status);

    //neu golfCourseId = null  hoac date nutll thi se tim tat ca
    @Query("SELECT t FROM TeeTime t WHERE (:golfCourseId IS NULL OR t.golfCourseId = :golfCourseId) " +
            "AND (:date IS NULL OR t.date = :date) AND t.isDeleted = false ORDER BY t.startTime ASC")
    List<TeeTime> findTeeTimesByDateAndGolfCourseId(String golfCourseId, LocalDate date, Pageable pageable);

    List<TeeTime> findByStatusAndHeldAtBefore(TeeTimeStatus teeTimeStatus, LocalDateTime expiredTime);

    @Query("SELECT t FROM TeeTime t WHERE t.status = :teeTimeStatus AND t.date = :date")
    List<TeeTime> findByStatusAndDate(TeeTimeStatus teeTimeStatus, LocalDate date);

    @Query("SELECT count(t) FROM TeeTime t WHERE t.date = :date AND t.golfCourseId = :courseId AND t.isDeleted = false")
    int existsByDateAndGolfCourseId(LocalDate date, String courseId);
}
