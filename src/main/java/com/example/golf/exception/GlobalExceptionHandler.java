package com.example.golf.exception;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.enums.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        ErrorResponse error= ex.getErrorResponse();
        ApiResponse  apiResponse = new ApiResponse();
        apiResponse.setCode(error.getCode());
        apiResponse.setMessage(error.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse> handleException(Exception ex) {
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(ErrorResponse.UNCATEGORIZED_EXCEPTION.getCode());
//        apiResponse.setMessage(ErrorResponse.UNCATEGORIZED_EXCEPTION.getMessage());
//        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
//    }
}
