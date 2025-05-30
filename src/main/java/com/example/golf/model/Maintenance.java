package com.example.golf.model;

import com.example.golf.enums.MaintenanceStatus;
import com.example.golf.enums.MaintenanceType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "maintenance")
public class Maintenance extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    private MaintenanceType type; // GOLF_COURSE hoặc TOOL
    private String referenceId; // ID sân golf hoặc dụng cụ cần bảo trì
    private String description; // Mô tả công việc bảo trì
    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status; // PENDING, IN_PROGRESS, COMPLETED
    private String staffId; // nhan vien thuc hien bao tri
    private String externalProvider;     // Tên công ty bảo trì nếu thuê ngoài (có thể để trống)
    private LocalDateTime scheduledDate; // Ngày dự kiến bảo trì
    private LocalDateTime completedDate; // Ngày hoàn thành bảo trì

}

