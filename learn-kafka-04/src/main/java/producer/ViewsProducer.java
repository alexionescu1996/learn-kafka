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

public class ViewsProducer {

    final static Logger log = LoggerFactory.getLogger(ViewsProducer.class);

    public static final String TOPIC_NAME = "kinaction_views";

    public static final String KEY_SERIALIZER = "key.serializer";
    public static final String STRING_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String VALUE_SERIALIZER = "value.serializer";
    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";

    public static void main(String[] args)
            throws ExecutionException, InterruptedException {

        Properties properties = new Properties();

        properties.put(BOOTSTRAP_SERVERS, "localhost:9092,localhost:9093");

        properties.put(KEY_SERIALIZER, STRING_SERIALIZER);
        properties.put(VALUE_SERIALIZER, STRING_SERIALIZER);

        try (Producer<String, String> producer =
                     new KafkaProducer<>(properties)) {

            for (int i = 0; i < 6; i++) {
                ProducerRecord<String, String> record = getProducerRecord(i);

                producer.send(record, (metadata, exception) -> {
                    if (exception != null) {
                        log.error("kinaction_error :: {exception}", exception);
                    } else
                        log.info("kinaction_info offset :: {}, partition :: {}, value :: {}",
                                metadata.offset(), record.partition(), record.value());
                });
            }
        }

    }

    private static ProducerRecord<String, String> getProducerRecord(int i) {

        return i % 2 == 0 ?
                new ProducerRecord<>(
                        TOPIC_NAME,
                        1,
                        "view1",
                        "view1" + UUID.randomUUID()
                )
                :
                new ProducerRecord<>(
                        TOPIC_NAME,
                        2,
                        "view0",
                        "view0" + UUID.randomUUID()
                );
    }
}
