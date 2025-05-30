package com.example.golf.exception;

import com.example.golf.enums.ErrorResponse;
import lombok.Data;

@Data
public class AppException extends RuntimeException {

    private ErrorResponse errorResponse;

    public AppException(ErrorResponse error)
    {
        super(error.getMessage());
        this.errorResponse = error;
    }
}
