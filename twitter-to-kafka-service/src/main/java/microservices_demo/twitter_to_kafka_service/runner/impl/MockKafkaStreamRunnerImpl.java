package microservices_demo.twitter_to_kafka_service.runner.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices_demo.config.TwitterToKafkaServiceConfigData;
import microservices_demo.twitter_to_kafka_service.model.TweetDTO;
import microservices_demo.twitter_to_kafka_service.runner.MockStreamRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import microservices_demo.twitter_to_kafka_service.model.TweetConstants;
import microservices_demo.twitter_to_kafka_service.runner.StreamRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "true")
public class MockKafkaStreamRunnerImpl implements StreamRunner , MockStreamRunner {

    @Override
    public void start() throws TwitterException {

    }

    @Override
    public List<TweetDTO> getTweets() throws TwitterException, FileNotFoundException {
        return prepareTweetListFromCsv();
    }

    private List<TweetDTO> prepareTweetListFromCsv() throws FileNotFoundException {
       List<TweetDTO> tweetDTOList = new ArrayList<>();
        String line = "";
        final String splitBy = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader(TweetConstants.DATASET_PATH));
            while ((line = br.readLine()) != null) {
                final TweetDTO tweetDTO = TweetDTO.getInstance(line.split(splitBy));
                tweetDTOList.add(tweetDTO);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return tweetDTOList;
    }
}
