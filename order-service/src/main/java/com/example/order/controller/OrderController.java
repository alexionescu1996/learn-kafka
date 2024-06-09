package com.example.order.controller;

import com.example.common.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        // Here, you can set the order status and handle the order object as needed
        kafkaTemplate.send("orders-topic", order);

        return ResponseEntity.ok("Order processed");
    }
}
