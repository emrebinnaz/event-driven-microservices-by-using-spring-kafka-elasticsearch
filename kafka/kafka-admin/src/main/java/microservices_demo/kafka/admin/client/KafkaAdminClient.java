package microservices_demo.kafka.admin.client;

import org.apache.kafka.clients.admin.TopicListing;

import java.util.Collection;

public interface KafkaAdminClient {
    void createTopics() throws InterruptedException;
    void checkTopicsCreated() throws InterruptedException;
    Collection<TopicListing> getTopics();
    void checkSchemaRegistry();
}
