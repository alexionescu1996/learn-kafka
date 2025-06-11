import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AlertKeySerde
        implements Serializer<Alert>, Deserializer<Alert> {

    @Override
    public byte[] serialize(String topic, Alert key) {
        if (key == null)
            return null;

        return key.getStageId()
                .getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Alert deserialize(String s, byte[] bytes) {
        return null;
    }

    @Override
    public void configure(final Map<String, ?> configs, final boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }

}
