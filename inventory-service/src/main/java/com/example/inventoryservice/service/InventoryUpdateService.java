package com.example.inventoryservice.service;

import com.example.common.model.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryUpdateService {

    @KafkaListener(topics = "orders-topic", groupId = "inventory-group")
    public void processOrder(Order order) {
        // Logic to update inventory
        System.out.println("Received order for processing: " + order);
    }
}

