package microservices_demo.twitter_to_kafka_service.mapper;

import microservices_demo.kafka.avro.model.TweetAvroModel;
import lombok.RequiredArgsConstructor;
import microservices_demo.twitter_to_kafka_service.model.TweetDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TweetDtoToAvroMapper implements BaseAvroMapper<TweetAvroModel, TweetDTO>{
    private final TweetLabelAvroMapper tweetLabelAvroMapper;

    @Override
    public TweetAvroModel mapFrom(TweetDTO tweetDTO) {
        return TweetAvroModel.newBuilder()
                .setTweetId(tweetDTO.getTweetId())
                .setLabel(tweetLabelAvroMapper.mapFrom(tweetDTO.getTweetLabel()))
                .setText(tweetDTO.getText())
                .build();
    }

    @Override
    public List<TweetAvroModel> mapListFrom(List<TweetDTO> source) {
        List<TweetAvroModel> modelList = new ArrayList<>();
        for (TweetDTO tweet : source) {
            modelList.add(mapFrom(tweet));
        }
        return modelList;
    }
}
