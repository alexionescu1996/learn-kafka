package producer;

import callback.AlertCallback;
import model.Alert;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import partitioner.AlertLevelPartitioner;
import serde.AlertKeySerde;

import java.util.Properties;
import java.util.Random;

public class AlertProducer {

    public static void main(String[] args) {

        sendMessage();
    }

    public static void sendMessage() {
        Properties properties = new Properties();

        properties.put("bootstrap.servers",
                "localhost:9092,localhost:9093");

        properties.put("key.serializer",
                AlertKeySerde.class.getName());
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        properties.put("partitioner.class",
                AlertLevelPartitioner.class.getName());

        try (Producer<Alert, String> producer =
                     new KafkaProducer<>(properties)) {

            for (int i = 0; i < 5; i++) {

                Alert alert = new Alert(
                        "Stage 0 Critical",
                        "Critical",
                        "Stage 0",
                        new Random().nextInt()
                );

                ProducerRecord<Alert, String> producerRecord =
                        new ProducerRecord<>(
                                "kinaction_alert",
                                alert,
                                alert.getAlertMessage()
                        );

                producer.send(producerRecord,
                        new AlertCallback());
            }
        }
    }
}
