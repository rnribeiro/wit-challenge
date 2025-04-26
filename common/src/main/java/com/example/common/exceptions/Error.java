package com.example.common.exceptions;


import org.springframework.http.HttpStatus;

public enum Error {

    DIVISION_BY_ZERO("Division by zero error.", HttpStatus.BAD_REQUEST),
    INVALID_OPERAND("Invalid input: unable to parse numbers.", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);

    private String message;
    private final HttpStatus httpStatus;

    Error(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
