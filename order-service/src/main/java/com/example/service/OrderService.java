package com.example.service;

import com.example.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Value("${topic.name}")
    public static String ORDERS_TOPIC;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void process(Order order) {
        kafkaTemplate.send(ORDERS_TOPIC, order);
    }
}
