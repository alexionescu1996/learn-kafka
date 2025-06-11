import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class DemoProduceCallback
        implements Callback {

    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        System.out.println(recordMetadata);
        if (e != null) {
            e.printStackTrace();
        }
        System.out.println("Done");
    }
}
