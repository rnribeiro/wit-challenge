package com.example.rest.controller;

import com.example.common.model.CalculationResponse;
import com.example.rest.service.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * CalculatorController handles HTTP requests for calculator operations.
 * It uses the CalculatorService to perform calculations and returns the results.
 */
@RestController
public class CalculatorController {

    // Logger for this class
    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
    // CalculatorService instance for performing calculations.
    private final CalculatorService calculatorService;

    @Autowired
    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/sum")
    public ResponseEntity<CalculationResponse> sum(
            @RequestParam String a,
            @RequestParam String b) {
        String requestId = MDC.get("requestId");
        logRequest("sum", a, b, requestId);
        CalculationResponse response = calculatorService.calculate("sum", a, b, UUID.fromString(requestId));
        return response.toResponseEntity();
    }

    @GetMapping("/subtraction")
    public ResponseEntity<CalculationResponse> subtract(
            @RequestParam String a,
            @RequestParam String b) {
        String requestId = MDC.get("requestId");
        logRequest("subtraction", a, b, requestId);
        CalculationResponse response = calculatorService.calculate("subtraction", a, b, UUID.fromString(requestId));
        return response.toResponseEntity();
    }

    @GetMapping("/multiplication")
    public ResponseEntity<CalculationResponse> multiply(
            @RequestParam String a,
            @RequestParam String b) {
        String requestId = MDC.get("requestId");
        logRequest("multiplication", a, b, requestId);
        CalculationResponse response = calculatorService.calculate("multiplication", a, b, UUID.fromString(requestId));
        return response.toResponseEntity();
    }

    @GetMapping("/division")
    public ResponseEntity<CalculationResponse> divide(
            @RequestParam String a,
            @RequestParam String b) {
        String requestId = MDC.get("requestId");
        logRequest("division", a, b, requestId);
        CalculationResponse response = calculatorService.calculate("division", a, b, UUID.fromString(requestId));
        return response.toResponseEntity();
    }

    private void logRequest(String operation, String a, String b, String requestId) {
        logger.info("New request received [ID={}]. Operation: {}, a: {}, b: {}", requestId, operation, a, b);
    }
}