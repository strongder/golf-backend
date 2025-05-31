package com.example.golf.model;


import com.example.golf.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String avatar;
    private String fullName;
    private String phone;
    private String password;
    private String provider; // google, facebook, local
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    private UserRole role; // ADMIN, STAFF, GOlFER
}
