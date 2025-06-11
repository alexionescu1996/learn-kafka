package com.example.learn_kafka_07;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {
                "test-topic"
        },
        controlledShutdown = true
)
public class KafkaIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private KafkaConsumer<String, String> consumer;

    @AfterEach
    void tearDown() {
        if (consumer != null) {
            consumer.wakeup();
            consumer.close(Duration.ofSeconds(2));
        }
    }

    @Test
    void test_producer_and_consumer() {

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
                "test-group",
                "true",
                embeddedKafkaBroker
        );

        var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(consumerProps);

        consumer = (KafkaConsumer<String, String>) consumerFactory.createConsumer();

        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "test-topic");

        producer();

        ConsumerRecords<String, String> records =
                KafkaTestUtils.getRecords(consumer, Duration.ofMillis(250));

        for (ConsumerRecord<String, String> record : records) {
            System.out.println(record.offset() + " " + record.partition() + " " + record.value());
        }


    }

    private void producer() {
        for (int i = 0; i < 5; i++) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
                    "test-topic",
                    "key" + i,
                    UUID.randomUUID().toString()
            );
            kafkaTemplate.send(producerRecord);
        }
    }

}
