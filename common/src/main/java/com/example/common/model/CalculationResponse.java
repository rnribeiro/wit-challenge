package com.example.common.model;

import com.example.common.exceptions.Error;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

public class CalculationResponse {
    private UUID requestId;
    private BigDecimal result;
    private Error error;

    public CalculationResponse() {}

    public CalculationResponse(BigDecimal result) {
        this.result = result;
    }

    public CalculationResponse(Error error) {
        this.error = error;
    }

    // Getters and Setters
    public UUID getRequestId() { return requestId; }
    public void setRequestId(UUID requestId) { this.requestId = requestId; }
    public BigDecimal getResult() { return result; }
    public void setResult(BigDecimal result) { this.result = result; }
    public Error getError() { return error; }
    public void setError(Error error) { this.error = error; }

    public ResponseEntity<CalculationResponse> toResponseEntity() {
        if (error != null) {
            return ResponseEntity.status(error.getHttpStatus()).body(this);
        } else {
            return ResponseEntity.ok(this);
        }
    }

}

