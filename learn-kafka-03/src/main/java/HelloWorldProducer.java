import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Properties;

public class HelloWorldProducer {

    static final Logger log =
            LoggerFactory.getLogger(HelloWorldProducer.class);


    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put("bootstrap.servers",
                "localhost:9092,localhost:9093,localhost:9094");

        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.LongSerializer");

        properties.put("value.serializer",
                "io.confluent.kafka.serializers.KafkaAvroSerializer");

        properties.put("schema.registry.url",
                "http://localhost:8081");

        try (Producer<Long, Alert> producer = new KafkaProducer<>(properties)) {

            Alert alert1 = new Alert(5L,
                    Instant.now().toEpochMilli(),
                    AlertStatus.Major);

            Alert alert2 = new Alert(6L,
                    Instant.now().toEpochMilli(),
                    AlertStatus.Warning);

            Alert alert3 = new Alert(7L,
                    Instant.now().toEpochMilli(),
                    AlertStatus.Minor);

            List<Alert> alerts = List.of(alert1, alert2, alert3);

            for (Alert alert : alerts) {
                log.info("kinaction_info Alert  :: {}", alert);

//                key = sensorId(), value = Alert object
                ProducerRecord<Long, Alert> producerRecord =
                        new ProducerRecord<>(
                                "kinaction_schematest",
                                alert.getSensorId(),
                                alert
                        );

                producer.send(producerRecord);
            }
        }
    }
}
