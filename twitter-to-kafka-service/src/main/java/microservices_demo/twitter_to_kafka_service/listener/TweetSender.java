package microservices_demo.twitter_to_kafka_service.listener;

import microservices_demo.twitter_to_kafka_service.model.TweetDTO;

import java.util.List;

public interface TweetSender {

    void sendTweetsToKafkaProducer(List<TweetDTO> tweets);
}
