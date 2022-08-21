package microservices_demo.twitter_to_kafka_service.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices_demo.config.KafkaConfigData;
import microservices_demo.kafka.admin.client.KafkaAdminClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaStreamInitializerImpl implements StreamInitializer{
    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    @Override
    public void init() throws InterruptedException {
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistry();
        log.info("Topics with the name {} is ready for operations", kafkaConfigData.getTopicNamesToCreate().toArray());
    }
}
