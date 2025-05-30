package com.example.golf.enums;

import lombok.Getter;

@Getter
public enum ErrorResponse {
    ENTITY_EXISTED(1001,"entity exist"),
    ENTITY_NOT_EXISTED(1002,"entity not exist"),
    INVALID_REQUEST(1003,"invalid request"),
    INVALID_PARAMETER(1004,"invalid parameter"),
    INVALID_STATUS_TRANSITION(1005,"invalid status transition"),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    INVALID_REQUEST_PARAMETERS(1006, "Invalid request parameters"),
    TRANSACTION_NOT_EXISTED(1007, "Transaction not existed"),
    TEE_TIME_NOT_AVAILABLE(1008, "Tee time not available"),
    INVALID_PASSWORD(1009, "Invalid password"),;
    ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private final int code;
    private final String message;

}
