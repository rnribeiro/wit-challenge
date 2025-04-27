package com.example.calculator.service;

import com.example.common.model.CalculationRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
    Service class to solve a calculation request
*/
@Service
public class CalculationService {

    // Method to solve calculation
    public BigDecimal calculate(CalculationRequest request) {
        return switch (request.getOperation().toLowerCase()) {
            case "sum" -> request.getA().add(request.getB());
            case "subtraction" -> request.getA().subtract(request.getB());
            case "multiplication" -> request.getA().multiply(request.getB());
            case "division" -> request.getA().divide(request.getB(), 10, RoundingMode.HALF_UP);
            default -> throw new IllegalArgumentException("Invalid operation");
        };
    }
}