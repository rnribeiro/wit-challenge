package com.example.rest.service;

import com.example.common.error.DefaultErrors;
import com.example.common.model.CalculationRequest;
import com.example.common.model.CalculationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * CalculatorService is responsible for handling calculation requests.
 * It sends requests to a Kafka topic and listens for responses.
 */
@Service
public class CalculatorService {

    @Value("${spring.kafka.topic.request-topic}")
    private String requestTopic;

    // Logger for logging messages
    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);
    // Kafka template for sending messages to Kafka topics
    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;
    // Listener for handling responses from Kafka topics
    private final ResponseListener responseListener;

    /**
     * Constructor for CalculatorService.
     *
     * @param kafkaTemplate    Kafka template for sending messages
     * @param responseListener Listener for handling responses
     */
    @Autowired
    public CalculatorService(KafkaTemplate<String, CalculationRequest> kafkaTemplate, ResponseListener responseListener) {
        this.kafkaTemplate = kafkaTemplate;
        this.responseListener = responseListener;
    }

    /**
     * Processes a calculation request by sending it to a Kafka topic and waiting for the response.
     *
     * @param operation the operation to perform (e.g., "sum", "subtract", "multiply", "divide")
     * @param a         the first operand
     * @param b         the second operand
     * @param requestId the unique request ID
     * @return the calculation response
     */
    public CalculationResponse calculate(String operation, String a, String b, UUID requestId) {

        // Create a new CalculationRequest object
        CalculationRequest request = new CalculationRequest();
        request.setRequestId(requestId);
        request.setOperation(operation);
        // Validate and set the operands
        try {
            request.setA(new java.math.BigDecimal(a));
        } catch (NumberFormatException e) {
            CalculationResponse response = new CalculationResponse(requestId, DefaultErrors.INVALID_OPERAND);
            response.getError().setMessage("Invalid input for a: " + a);
            return response;
        }
        try {
            request.setB(new java.math.BigDecimal(b));
        } catch (NumberFormatException e) {
            CalculationResponse response = new CalculationResponse(requestId, DefaultErrors.INVALID_OPERAND);
            response.getError().setMessage("Invalid input for b: " + b);
            return response;

        }

        // Create a CompletableFuture to handle the response
        CompletableFuture<CalculationResponse> future = responseListener.createFuture(requestId);
        kafkaTemplate.send(requestTopic, requestId.toString(), request)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Sent request [ID={}] to calculator module.", requestId);
                    } else {
                        logger.error("Failed to send calculation request: {}", requestId, ex);
                        future.completeExceptionally(ex);
                    }
                });


        try {
            return future.get(10, java.util.concurrent.TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Error getting response from calculator module for request [ID={}]: {}", requestId, e.getMessage());
            CalculationResponse res = new CalculationResponse(requestId, DefaultErrors.INTERNAL_SERVER_ERROR);
            res.getError().setMessage(String.format("Error getting response from calculator module for request [ID=%s]: %s", requestId, e.getMessage()));
            return res;
        }
    }
}