package com.example.calculator.listener;

import com.example.calculator.service.CalculationService;
import com.example.common.error.DefaultErrors;
import com.example.common.model.CalculationRequest;
import com.example.common.model.CalculationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CalculationListener {

    private static final Logger logger = LoggerFactory.getLogger(CalculationListener.class);
    private final CalculationService calculationService;
    private final KafkaTemplate<String, CalculationResponse> kafkaTemplate;

    @Autowired
    public CalculationListener(CalculationService calculationService, KafkaTemplate<String, CalculationResponse> kafkaTemplate) {
        this.calculationService = calculationService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${spring.kafka.topic.request-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(CalculationRequest request) {
        UUID requestId = request.getRequestId();
        MDC.put("requestId", requestId.toString());
        try {
            logger.info("Received calculation request: {}", requestId);
            CalculationResponse response = calculationService.calculate(request);
            kafkaTemplate.send("calculation-response", requestId.toString(), response);
            logger.info("Sent calculation response: {}", requestId);
        } catch (ArithmeticException e) {
            logger.error("Division by zero error for requestId: {}", requestId);
            CalculationResponse response = new CalculationResponse(requestId, DefaultErrors.DIVISION_BY_ZERO);
            kafkaTemplate.send("calculation-response", requestId.toString(), response);
        } catch (Exception e) {
            logger.error("Error processing request {}: {}", requestId, e.getMessage());
            CalculationResponse response = new CalculationResponse(requestId, DefaultErrors.INTERNAL_SERVER_ERROR);
            kafkaTemplate.send("calculation-response", requestId.toString(), response);
        } finally {
            MDC.clear();
        }
    }
}