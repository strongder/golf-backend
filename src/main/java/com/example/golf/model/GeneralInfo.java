package com.example.golf.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
public class GeneralInfo extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Double latitude; // toa do
    private String imageUrl;

}
