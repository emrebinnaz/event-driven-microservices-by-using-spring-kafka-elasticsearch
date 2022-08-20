package microservices_demo.kafka.admin.constants.error_messages;

public class KafkaClientErrorMessages {

    public static final String REACHED_MAX_RETRY_COUNT = "Reached max number of retry for creating kafka topic(s)";
    public static final String SLEEPING_FOR_WAITING_NEW_TOPICS = "Error while sleeping for waiting new created topics !!!";

    private KafkaClientErrorMessages() {

    }
}
