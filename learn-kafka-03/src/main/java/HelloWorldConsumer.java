import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.checkerframework.checker.units.qual.K;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;


public class HelloWorldConsumer {

    final static Logger log =
            LoggerFactory.getLogger(HelloWorldProducer.class);

    private volatile boolean keepConsuming = true;

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put("bootstrap.servers",
                "localhost:9092,localhost:9093,localhost:9094");

        properties.put("group.id", "kinaction_helloconsumer");

        properties.put("auto.offset.reset", "earliest");

        properties.put("specific.avro.reader", "true");

        properties.put("key.deserializer",
                "org.apache.kafka.common.serialization.LongDeserializer");

        properties.put("value.deserializer",
                "io.confluent.kafka.serializers.KafkaAvroDeserializer");

        properties.put("schema.registry.url", "http://localhost:8081");

        HelloWorldConsumer helloWorldConsumer = new HelloWorldConsumer();
        helloWorldConsumer.consume(properties);

        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(helloWorldConsumer::shutdown)
                );
    }

    private void shutdown() {
        keepConsuming = false;
    }

    private void consume(Properties properties) {
        try (KafkaConsumer<Long, Alert> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(
                    List.of("kinaction_schematest")
            );

            while (keepConsuming) {
                ConsumerRecords<Long, Alert> records =
                        consumer.poll(Duration.ofMillis(250));

                for (ConsumerRecord<Long, Alert> record : records) {
                    log.info("kinaction_info offset :: {}, kinaction_value :: {}",
                            record.offset(),
                            record.value());
                }
            }
        }
    }
}
