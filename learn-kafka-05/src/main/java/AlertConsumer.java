import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class AlertConsumer {


    private static final Logger log = LoggerFactory.getLogger(AlertConsumer.class);

    private volatile boolean keepConsuming = true;

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers",
                "localhost:9092,localhost:9093");


        properties.put("key.deserializer",
                AlertKeySerde.class.getName());
        properties.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");


        properties.put("enable.auto.commit", "true");

        properties.put("auto.offset.reset", "earliest");

        properties.put("group.id", "kinaction_alert_group_5");
        AlertConsumer alertConsumer = new AlertConsumer();
        alertConsumer.consume(properties);

        Runtime.getRuntime().addShutdownHook(new Thread(alertConsumer::shutdown));
    }

    private void consume(Properties properties) {
        try (KafkaConsumer<Alert, String> consumer = new KafkaConsumer<>(properties)) {

            consumer.subscribe(List.of("kinaction_alert"));

            while (keepConsuming) {
                var records = consumer.poll(Duration.ofMillis(250));
                for (ConsumerRecord<Alert, String> record : records) {
                    log.info("kinaction_info offset :: {}, value :: {}, partition :: {}",
                            record.offset(), record.value(), record.partition());
                }
            }
        }
    }

    private void shutdown() {
        keepConsuming = false;
    }

}
