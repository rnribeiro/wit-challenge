package com.example.calculator.service;

import com.example.common.model.CalculationRequest;
import com.example.common.model.CalculationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculationService {

    private static final Logger logger = LoggerFactory.getLogger(CalculationService.class);

    public CalculationResponse calculate(CalculationRequest request) {
        logger.info("Calculating {} with a={} and b={}", request.getOperation(), request.getA(), request.getB());
        BigDecimal result = switch (request.getOperation().toLowerCase()) {
            case "sum" -> request.getA().add(request.getB());
            case "subtraction" -> request.getA().subtract(request.getB());
            case "multiplication" -> request.getA().multiply(request.getB());
            case "division" -> request.getA().divide(request.getB(), 10, RoundingMode.HALF_UP);
            default -> {
                logger.error("Invalid operation: {} for requestId: {}", request.getOperation(), request.getRequestId());
                throw new IllegalArgumentException("Invalid operation");
            }
        };

        logger.info("Result for requestId {}: {}", request.getRequestId(), result);
        return new CalculationResponse(request.getRequestId(), result);
    }
}