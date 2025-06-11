package com.example.golf.repository;

import com.example.golf.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToolsRepository  extends JpaRepository<Tool, String> {

    @Query("SELECT t FROM Tool t WHERE t.isDeleted = false")
    List<Tool> findByToolDeleteFalse();

    @Query("SELECT t FROM Tool t WHERE t.type = 'GOLF_CLUB' AND t.isDeleted = false")
    List<Tool> findToolByType(String type);
}
