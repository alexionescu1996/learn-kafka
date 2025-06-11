package callback;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertCallback
        implements Callback {

    private static final Logger log =
            LoggerFactory.getLogger(AlertCallback.class);

    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {

        if (e != null) {
            log.error("kinaction_error", e);
        } else {
            log.info("kinaction_info offset :: {}, topic :: {}, timestamp :: {}",
                    recordMetadata.offset(), recordMetadata.topic(), recordMetadata.timestamp());
        }
    }
}
