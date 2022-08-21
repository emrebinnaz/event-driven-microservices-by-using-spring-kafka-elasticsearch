package microservices_demo.kafka.admin.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices_demo.config.KafkaConfigData;
import microservices_demo.config.RetryConfigData;
import microservices_demo.kafka.admin.constants.error_messages.KafkaClientErrorMessages;
import microservices_demo.kafka.admin.exception.KafkaClientException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaAdminClientImpl implements KafkaAdminClient{

    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;

    @Override
    public void createTopics() throws InterruptedException {
        CreateTopicsResult createTopicsResult;
        //execute calls a method with retry logic configured in the Retry Config.
        try {
            createTopicsResult = retryTemplate.execute(this::doCreateTopics);
        }catch (Throwable t) {
            throw new KafkaClientException(KafkaClientErrorMessages.REACHED_MAX_RETRY_COUNT, t);
        }
        checkTopicsCreated(); // double check for creating topics
    }

    @Override
    public void checkTopicsCreated() throws InterruptedException {
        Collection<TopicListing> topics = getTopics();
        int retryCount = 1;
        final Integer maxRetry = retryConfigData.getMaxAttempts();
        final int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        for (String topicName :kafkaConfigData.getTopicNamesToCreate()) {
            while(!isTopicCreate(topics, topicName)) {
                checkMaxRetry(retryCount++, maxRetry);
                sleep(sleepTimeMs);
                sleepTimeMs *= multiplier;
                topics = getTopics();
            }
        }
    }

    private void checkMaxRetry(int retry, Integer maxRetry) {
       if(retry > maxRetry){
           throw new KafkaClientException(KafkaClientErrorMessages.REACHED_MAX_RETRY_COUNT);
       }
    }

    private void sleep(Long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException ie) {
            throw new KafkaClientException(KafkaClientErrorMessages.SLEEPING_FOR_WAITING_NEW_TOPICS);
        }
    }

    @Override
    public void checkSchemaRegistry() {
        // schema registry is important. call rest request in order to observe whether schema is up or not.
        int retryCount = 1;
        final Integer maxRetry = retryConfigData.getMaxAttempts();
        final int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        while (getSchemaRegistryStatus().is2xxSuccessful()) {
            checkMaxRetry(retryCount++, maxRetry);
            sleep(sleepTimeMs);
            sleepTimeMs *= multiplier;
        }
    }

    private HttpStatus getSchemaRegistryStatus() {
        try {
            return webClient.method(HttpMethod.GET)
                    .uri(kafkaConfigData.getSchemaRegistryUrl())
                    .retrieve()
                    .bodyToMono(HttpStatus.class)
                    .block(); // block for to be able to get results synchronously.
        } catch (Exception e) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }
    private boolean isTopicCreate(Collection<TopicListing> topics, String topicName) {
        if(topics == null) {
            return false;
        } else {
            return topics.stream().anyMatch(topic -> topic.name().equals(topicName));
        }
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        final List<String> topicNamesToCreate = kafkaConfigData.getTopicNamesToCreate();
        log.info("Creating {} topics, attempt {}", topicNamesToCreate.size(), retryContext.getRetryCount());

        final List<NewTopic> kafkaTopics = topicNamesToCreate
                .stream()
                .map(topic -> new NewTopic(
                    topic.trim(),
                    kafkaConfigData.getNumOfPartitions(),
                    kafkaConfigData.getReplicationFactor()))
                .collect(Collectors.toList());

        return adminClient.createTopics(kafkaTopics);
    }

    public Collection<TopicListing> getTopics() {
        Collection<TopicListing> topics;

        try {
            topics = retryTemplate.execute(this::doGetTopics);
        } catch (Throwable t) {
            throw new KafkaClientException(KafkaClientErrorMessages.REACHED_MAX_RETRY_COUNT, t);
        }
        return topics;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext)
            throws ExecutionException, InterruptedException {

        log.info("Reading kafka topoic {}, attempt {}", kafkaConfigData.getTopicNamesToCreate().toArray(),
                retryContext.getRetryCount());
        Collection<TopicListing> topics = adminClient.listTopics().listings().get();
        if(topics != null) {
            topics.forEach(topic -> log.debug("Topic with name {}", topic.name()));
        }
        return topics;
    }
}
