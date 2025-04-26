package com.example.common.model;

import java.math.BigDecimal;
import java.util.UUID;

public class CalculationRequest {
    private UUID requestId;
    private String operation;
    private BigDecimal a;
    private BigDecimal b;

    public CalculationRequest() {}

    // Getters and Setters
    public UUID getRequestId() { return requestId; }
    public void setRequestId(UUID requestId) { this.requestId = requestId; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    public BigDecimal getA() { return a; }
    public void setA(BigDecimal a) { this.a = a; }
    public BigDecimal getB() { return b; }
    public void setB(BigDecimal b) { this.b = b; }
}