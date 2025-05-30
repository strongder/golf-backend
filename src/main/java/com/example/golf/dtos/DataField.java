package com.example.golf.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataField {
    private String id;
    private String code;
    private String name;
    private String email;
    private String fullName;
    private String phone;
}
