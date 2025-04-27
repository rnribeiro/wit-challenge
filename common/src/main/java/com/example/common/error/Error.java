package com.example.common.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {

    @JsonProperty("code")
    private ErrorCode code;

    @JsonProperty("message")
    private String message;

    public Error() {
    }

    public Error(ErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return switch (code) {
            case ErrorCode.INVALID_OPERAND, ErrorCode.DIVISION_BY_ZERO -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getCode() {
        return code;
    }
}
