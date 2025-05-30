package com.example.golf.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    @Builder.Default
    private int code = 1000;
    private String message;
    private Object data;

    public static ApiResponse success(Object result) {
        return ApiResponse.builder()
                .code(1000)
                .message("Success")
                .data(result)
                .build();
    }
}
