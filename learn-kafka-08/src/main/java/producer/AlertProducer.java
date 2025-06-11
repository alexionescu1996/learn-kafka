package producer;

import model.Alert;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class AlertProducer {

    public void sendMessage(Properties properties) throws ExecutionException, InterruptedException {
        properties.put("partitioner.class",
                "partitioner.AlertLevelPartitioner");

        try (Producer<Alert, String> producer = new KafkaProducer<>(properties)) {
            Alert alert = new Alert(
                    1,
                    "Stage 1",
                    "CRITICAL",
                    "Stage 1 stopped"
            );

            ProducerRecord<Alert, String> producerRecord = new ProducerRecord<>(
                    "kinaction_alert",
                    alert,
                    alert.getAlertMessage()
            );

            RecordMetadata metadata =
                    producer.send(producerRecord).get();
            metadata.offset();
        }
    }
}
