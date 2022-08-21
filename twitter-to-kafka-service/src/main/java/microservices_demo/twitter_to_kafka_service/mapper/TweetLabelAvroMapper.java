package microservices_demo.twitter_to_kafka_service.mapper;

import microservices_demo.kafka.avro.model.TweetLabelAvroModel;
import microservices_demo.twitter_to_kafka_service.constants.ErrorMessages;
import microservices_demo.twitter_to_kafka_service.model.TweetLabel;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TweetLabelAvroMapper implements BaseAvroMapper<TweetLabelAvroModel, TweetLabel> {
    private static final Map<TweetLabel, TweetLabelAvroModel> TWEET_LABEL_AVRO_MODEL_MAP = new HashMap<>();

    @Override
    public TweetLabelAvroModel mapFrom(TweetLabel source) {
        if(TWEET_LABEL_AVRO_MODEL_MAP.get(source) != null) {
            return TWEET_LABEL_AVRO_MODEL_MAP.get(source);

        } else {
            final Optional<TweetLabelAvroModel> optionalTweetLabelAvroModel = Arrays.stream(TweetLabelAvroModel.values())
                    .filter(label -> label.toString().equals(source.getTextForm()))
                    .findFirst();

            optionalTweetLabelAvroModel.ifPresent(tweetLabelAvroModel -> TWEET_LABEL_AVRO_MODEL_MAP.put(source, tweetLabelAvroModel));
            if(optionalTweetLabelAvroModel.isPresent()) {
                return optionalTweetLabelAvroModel.get();
            } else {
                throw new RuntimeException(ErrorMessages.NOT_FOUND_TWEET_LABEL);
            }
        }

    }

    @Override
    public List<TweetLabelAvroModel> mapListFrom(List<TweetLabel> source) {
        List<TweetLabelAvroModel> models = new ArrayList<>();
        for (TweetLabel label : source) {
            models.add(mapFrom(label));
        }
        return models;
    }
}
