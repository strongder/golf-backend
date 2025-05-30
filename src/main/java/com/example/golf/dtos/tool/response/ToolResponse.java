package com.example.golf.dtos.tool.response;

import com.example.golf.enums.ToolStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
public class ToolResponse {
    private String id;
    private String code;
    private String name; // Tên dụng cụ (Gậy golf, Máy đo khoảng cách...)
    private int quantity;
    private String type;
    private double rentPrice; // Giá thuê
    private String provider; // Nhà cung cấp
    private ToolStatus status; // AVAILABLE, IN_USE, MAINTENANCE
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private String createdBy;
    private String updatedBy;
}
