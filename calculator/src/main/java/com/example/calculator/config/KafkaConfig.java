package com.example.calculator.config;

import com.example.common.model.CalculationRequest;
import com.example.common.model.CalculationResponse;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    // Set up Kafka topics for the calculator service.
    @Bean
    public NewTopic calculatorRequestTopic() {
        return TopicBuilder.name("calculation-request").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic calculatorResponseTopic() {
        return TopicBuilder.name("calculation-response").partitions(1).replicas(1).build();
    }

    // Set up Kafka template for sending CalculationRequest messages.
    @Bean
    public KafkaTemplate<String, CalculationResponse> responseKafkaTemplate(ProducerFactory<String, CalculationResponse> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CalculationRequest> calculatorKafkaListenerContainerFactory(
            ConsumerFactory<String, CalculationRequest> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, CalculationRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}