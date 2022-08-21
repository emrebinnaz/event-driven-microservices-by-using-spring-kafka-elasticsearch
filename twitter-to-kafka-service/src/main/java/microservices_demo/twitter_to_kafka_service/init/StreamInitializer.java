package microservices_demo.twitter_to_kafka_service.init;

public interface StreamInitializer {
    void init() throws InterruptedException;
}
