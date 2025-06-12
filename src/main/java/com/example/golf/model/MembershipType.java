package com.example.golf.model;

import com.example.golf.config.mapper.ListToJsonConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@Entity
public class MembershipType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private double price; // in USD
    private int duration; // in months
    private int bookingBeforeDay; // number of days before booking allowed
    @Convert(converter = ListToJsonConverter.class)
    @Column(columnDefinition = "json")
    private List<String> benefits; // comma-separated list of benefits
    private int discount; // percentage discount for members
}
