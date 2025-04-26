package com.example.calculator.listener;

import com.example.calculator.service.CalculationService;
import com.example.common.exceptions.Error;
import com.example.common.exceptions.ZeroDivisionException;
import com.example.common.model.CalculationRequest;
import com.example.common.model.CalculationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

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
        MDC.put("requestId", request.getRequestId().toString());
        try {
            logger.info("Received calculation request: {}", request.getRequestId());
            CalculationResponse response = calculationService.calculate(request);
            kafkaTemplate.send("calculation-response", request.getRequestId().toString(), response);
            logger.info("Sent calculation response: {}", request.getRequestId());
        } catch (ZeroDivisionException e) {
            logger.error("ZeroDivisionException for request {}: {}", request.getRequestId(), e.getMessage());
            CalculationResponse response = new CalculationResponse(Error.DIVISION_BY_ZERO);
            response.setRequestId(request.getRequestId());
            kafkaTemplate.send("calculation-response", request.getRequestId().toString(), response);
        } catch (Exception e) {
            logger.error("Error processing request {}: {}", request.getRequestId(), e.getMessage());
            CalculationResponse response = new CalculationResponse(Error.INTERNAL_ERROR);
            kafkaTemplate.send("calculation-response", request.getRequestId().toString(), response);
        } finally {
            MDC.clear();
        }
    }
}