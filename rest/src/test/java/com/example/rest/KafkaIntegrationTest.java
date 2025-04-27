package com.example.rest;

import com.example.common.model.CalculationRequest;
import com.example.calculator.config.KafkaConfig;
import com.example.common.model.CalculationResponse;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;


import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"calculation-request"})
@DirtiesContext
@Import({KafkaConfig.class})
class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, CalculationRequest> requestKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, CalculationResponse> responseKafkaTemplate;

    private final BlockingQueue<ConsumerRecord<String, CalculationRequest>> requestQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<ConsumerRecord<String, CalculationResponse>> responseQueue = new LinkedBlockingQueue<>();

    @KafkaListener(topics = "calculation-request", groupId = "test-group")
    public void requestListener(ConsumerRecord<String, CalculationRequest> record) {
        requestQueue.add(record);
    }

    @KafkaListener(topics = "calculation-response", groupId = "test-group")
    public void responseListener(ConsumerRecord<String, CalculationResponse> record) {
        responseQueue.add(record);
    }

    @Test
    void test() throws Exception {
        // Prepare test data
        UUID requestId = UUID.randomUUID();
        CalculationRequest request = new CalculationRequest();
        request.setRequestId(requestId);
        request.setOperation("sum");
        request.setA(new BigDecimal("2"));
        request.setB(new BigDecimal("3"));

        // Send request
        requestKafkaTemplate.send("calculation-request", requestId.toString(), request).get();

        // Wait for response
        ConsumerRecord<String, CalculationRequest> record = requestQueue.poll(10, TimeUnit.SECONDS);
        assertNotNull(record, "Response not received within timeout");
        CalculationRequest receivedRequest = record.value();

        // Verify response
        assertEquals(requestId, receivedRequest.getRequestId());
        assertEquals("sum", receivedRequest.getOperation());
        assertEquals(new BigDecimal("2"), receivedRequest.getA());
        assertEquals(new BigDecimal("3"), receivedRequest.getB());

        // Send response
        CalculationResponse response = new CalculationResponse();
        response.setRequestId(requestId);
        response.setResult(new BigDecimal("5"));
        responseKafkaTemplate.send("calculation-response", requestId.toString(), response).get();

        // Wait for response
        ConsumerRecord<String, CalculationResponse> responseRecord = responseQueue.poll(10, TimeUnit.SECONDS);
        assertNotNull(responseRecord, "Response not received within timeout");
        CalculationResponse receivedResponse = responseRecord.value();

        // Verify response
        assertEquals(requestId, receivedResponse.getRequestId());
        assertEquals(new BigDecimal("5"), receivedResponse.getResult());
    }

}