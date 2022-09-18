package microservices_demo.kafka.service;

import microservices_demo.kafka.avro.model.TweetAvroModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;

@RequiredArgsConstructor
@Slf4j
@Service
public class TwitterKafkaProducerImpl implements KafkaProducer<String, TweetAvroModel> {
    private final KafkaTemplate<String, TweetAvroModel> kafkaTemplate;

    @Override
    public void send(String topicName, String key, TweetAvroModel message) {
        log.info("Sending message = '{}' to topic = '{}'", message, topicName);
        final ListenableFuture<SendResult<String, TweetAvroModel>> listenableFuture = kafkaTemplate.send(topicName, key, message);
        // register callback methods for handling events when the response return.
        //send method of Kafka template is async operation so it returns listenable future
        addCallback(topicName, message, listenableFuture);
    }

    private void addCallback(String topicName, TweetAvroModel message,
                             ListenableFuture<SendResult<String, TweetAvroModel>> listenableFuture) {

        listenableFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending message {} to topic {}", message.toString(), topicName, ex);
            }

            @Override
            public void onSuccess(SendResult<String, TweetAvroModel> result) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                log.debug("Received new metadata. Topic : {}. \n Partition {}. \n Offset {}; \n Timestamp: {} \n, at time {}",
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset(),
                        recordMetadata.timestamp(),
                        System.nanoTime());

            }
        });
    }

    @PreDestroy
    @Override
    public void close() {
        if(kafkaTemplate != null) {
            log.info("Closing kafka producer.");
            kafkaTemplate.destroy();
        }
    }
}
