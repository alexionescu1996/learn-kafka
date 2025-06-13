package com.example.service;

import com.example.model.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    final static Logger log =
            LoggerFactory.getLogger(OrderService.class);

    @Value("${topic.name}")
    public String ORDERS_TOPIC;

    private final KafkaTemplate<Long, Order> kafkaTemplate;

    @Autowired
    public OrderService(KafkaTemplate<Long, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void process(Order order) {

        CompletableFuture<SendResult<Long, Order>> future = kafkaTemplate.send(
                ORDERS_TOPIC,
                order.getId(),
                order
        );

        future.whenComplete((result, ex) -> {
            RecordMetadata recordMetadata = result.getRecordMetadata();
            if (ex != null) {
                log.error("order_error :: {ex}", ex);
            } else {
                log.info("offset :: {}, topic :: {}, partition :: {}",
                        recordMetadata.offset(), recordMetadata.topic(), recordMetadata.partition());
            }
        });

    }

}
