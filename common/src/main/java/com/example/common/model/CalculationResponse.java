package com.example.common.model;

import com.example.common.error.Error;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalculationResponse {

    private UUID requestId;
    private BigDecimal result;
    private Error error;

    /**
     * Default constructor for JSON deserialization.
     */
    public CalculationResponse() {  }

    public CalculationResponse(UUID requestId, BigDecimal result) {
        this.requestId = requestId;
        this.result = result;
    }

    public CalculationResponse(UUID requestId, Error error) {
        this.requestId = requestId;
        this.error = error;
    }

    public ResponseEntity<CalculationResponse> toResponseEntity() {
        if (error != null) {
            return ResponseEntity.status(error.getHttpStatus()).body(this);
        } else {
            return ResponseEntity.ok(this);
        }
    }

    // Getters and Setters
    public UUID getRequestId() { return requestId; }
    public void setRequestId(UUID requestId) { this.requestId = requestId; }
    public BigDecimal getResult() { return result; }
    public void setResult(BigDecimal result) { this.result = result; }
    public Error getError() { return error; }
    public void setError(Error error) { this.error = error; }

}

