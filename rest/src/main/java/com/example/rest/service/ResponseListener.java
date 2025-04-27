package com.example.rest.service;

import com.example.common.model.CalculationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class listens for calculation responses from Kafka and completes the corresponding CompletableFuture.
 * It uses a ConcurrentHashMap to store futures associated with request IDs.
 */
@Component
public class ResponseListener {

    // Logger for logging messages
    private static final Logger logger = LoggerFactory.getLogger(ResponseListener.class);
    // Map to store CompletableFuture associated with request IDs
    private final Map<UUID, CompletableFuture<CalculationResponse>> futures = new ConcurrentHashMap<>();

    // Creates a CompletableFuture for the given request ID and stores it in the futures map.
    public CompletableFuture<CalculationResponse> createFuture(UUID requestId) {
        CompletableFuture<CalculationResponse> future = new CompletableFuture<>();
        futures.put(requestId, future);
        return future;
    }

    // Listens for calculation responses from Kafka and completes the corresponding CompletableFuture.
    @KafkaListener(topics = "${spring.kafka.topic.response-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(CalculationResponse response) {
        CompletableFuture<CalculationResponse> future = futures.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        }
        logger.info("Received response from calculator module for request [ID={}]}", response.getRequestId());
    }
}