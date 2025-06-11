package producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AuditProducer {

    private static final Logger log =
            LoggerFactory.getLogger(AuditProducer.class);

    public static void main(String[] args)
            throws ExecutionException, InterruptedException {

        Properties properties = getProperties();

        try (Producer<String, String> producer = new KafkaProducer<>(properties)) {
            for (int i = 0; i < 5; i++) {
                ProducerRecord<String, String> producerRecord =
                        new ProducerRecord<>(
                                "kinaction_audit",
                                null,
                                ("audit event " + UUID.randomUUID())
                        );

                RecordMetadata result = producer.send(producerRecord).get();

                log.info("kinaction_info offset :: {}, topic :: {}, timestamp :: {}",
                        result.offset(), result.topic(), result.timestamp());
            }
        }
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers",
                "localhost:9092,localhost:9093,localhost:9094");

        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        properties.put("acks", "all");
        properties.put("retries", 3);
        properties.put("max.in.flight.requests.per.connection", "1");

        return properties;
    }
}
