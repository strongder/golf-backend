package com.example.golf.dtos.search;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Data
public class BaseSearchRequest {
    private String key;
    private String value;
    private String key2;
    private String value2;
    private Integer page;
    private Integer size;
    private String status;
    private LocalDate date;
    private String sortBy = "createdAt";
    private String sortDir = "desc";
    private LocalDate startDate;
    private LocalDate endDate;

}
