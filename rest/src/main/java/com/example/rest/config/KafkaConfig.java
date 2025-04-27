package com.example.rest.config;

import com.example.common.model.CalculationRequest;
import com.example.common.model.CalculationResponse;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;

@Configuration
@EnableKafka
public class KafkaConfig {

    // Set up Kafka topics for the calculator service.
    @Bean
    public NewTopic restRequestTopic() {
        return TopicBuilder.name("calculation-request").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic restResponseTopic() {
        return TopicBuilder.name("calculation-response").partitions(1).replicas(1).build();
    }

    // Set up Kafka template for sending CalculationResponse messages.
    @Bean
    public KafkaTemplate<String, CalculationRequest> requestKafkaTemplate(ProducerFactory<String, CalculationRequest> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CalculationResponse> restKafkaListenerContainerFactory(
            ConsumerFactory<String, CalculationResponse> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, CalculationResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}