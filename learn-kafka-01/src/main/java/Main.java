import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class Main {

    final static Logger log =
            LoggerFactory.getLogger(Main.class);

    private volatile boolean keepConsuming = true;


    public static void main(String[] args) {

        Properties kaProperties = new Properties();
        kaProperties.put("bootstrap.servers",
                "localhost:9092,localhost:9093,localhost:9094");

        kaProperties.put("group.id", "kinatction_helloconsumer");
        kaProperties.put("enable.auto.commit", true);
        kaProperties.put("auto.commit.interval.ms", "1000");
        kaProperties.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        kaProperties.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");


        Main main = new Main();
        main.consume(kaProperties);
        Runtime.getRuntime()
                .addShutdownHook(new Thread(main::shutdown));

    }

    private void consume(Properties properties) {
        try (KafkaConsumer<String, String> consumer =
                     new KafkaConsumer<>(properties)) {
            consumer.subscribe(
                    List.of(
                            "kinaction_helloworld"
                    )
            );

            while (keepConsuming) {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(250));
                for (ConsumerRecord<String, String> record :
                        records) {
                    log.info("kinaction_info offset :: {}, kinaction_value :: {}",
                            record.offset(), record.value());
                }
            }
        }
    }

    private void shutdown() {
        keepConsuming = false;
    }

}
