package microservices_demo.twitter_to_kafka_service.listener;

import microservices_demo.kafka.avro.model.TweetAvroModel;
import microservices_demo.kafka.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices_demo.config.KafkaConfigData;
import microservices_demo.twitter_to_kafka_service.mapper.TweetDtoToAvroMapper;
import microservices_demo.twitter_to_kafka_service.model.TweetDTO;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TwitterKafkaStatusListener extends StatusAdapter implements TweetSender{

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<String, TweetAvroModel> kafkaProducer;
    private final TweetDtoToAvroMapper tweetAvroMapper;

    @Override
    public void onStatus(Status status) {

    }

    public void sendTweetsToKafkaProducer(List<TweetDTO> tweets) {
        final List<TweetAvroModel> tweetAvroModels = tweetAvroMapper.mapListFrom(tweets);
        tweetAvroModels.forEach(tweetModel -> kafkaProducer
                .send(kafkaConfigData.getTopicName(), tweetModel.getLabel().toString(), tweetModel));
       //TODO: Send them as batch
    }
}
