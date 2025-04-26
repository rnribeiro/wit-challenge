package com.example.common.error;

public enum ErrorCode {

    INVALID_OPERAND("INVALID_OPERAND"),
    DIVISION_BY_ZERO("DIVISION_BY_ZERO"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
