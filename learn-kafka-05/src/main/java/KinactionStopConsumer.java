import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class KinactionStopConsumer
        implements Runnable {


    public static final String KINACTION_PROMOS_TOPIC = "kinaction_promos";
    public static final int BLOCKED_TIME_FOR_CURRENT_THREAD = 250;

    private final KafkaConsumer<String, String> consumer;

    private final AtomicBoolean stopping =
            new AtomicBoolean(false);

    final static Logger log = LoggerFactory.getLogger(KinactionStopConsumer.class);

    public KinactionStopConsumer(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    public static void main(String[] args) {

        KafkaConsumer<String, String> consumer = getStringStringKafkaConsumer();

        KinactionStopConsumer stopConsumer = new KinactionStopConsumer(consumer);
        Thread consumerThread = new Thread(stopConsumer);
        consumerThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown hook triggered. Stopping consumer...");
            stopConsumer.shutdown();
            try {
                consumerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));

    }

    private static KafkaConsumer<String, String> getStringStringKafkaConsumer() {
        Properties kaProperties = new Properties();

        kaProperties.put("bootstrap.servers",
                "localhost:9092,localhost:9093,,localhost:9094");

        kaProperties.put("group.id", "kinaction_stopconsumer");

        kaProperties.put("enable.auto.commit", "true");
        kaProperties.put("auto.commit.interval.ms", "1000");

        kaProperties.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        kaProperties.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        return new KafkaConsumer<>(kaProperties);
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(List.of(KINACTION_PROMOS_TOPIC));

            while (!stopping.get()) {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(BLOCKED_TIME_FOR_CURRENT_THREAD));

                for (ConsumerRecord<String, String> record : records) {
                    log.info("kinaction_info offset :: {}, value :: {}, partition :: {}",
                            record.offset(), record.value(), record.partition());
                }
            }
        } catch (WakeupException e) {
            if (!stopping.get()) {
                throw e;
            }
        } finally {
            consumer.close();
        }
    }


    public void shutdown() {
        stopping.set(true);
        consumer.wakeup();
    }
}
