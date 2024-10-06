package com.bekrenovr.ecommerce.orders.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${spring.kafka.topic}")
    public String topic;

    @Bean
    public NewTopic libraryEvents() {
        return TopicBuilder
                .name(topic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
