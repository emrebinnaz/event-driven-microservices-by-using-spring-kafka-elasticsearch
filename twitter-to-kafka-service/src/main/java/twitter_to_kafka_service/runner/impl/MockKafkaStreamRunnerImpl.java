package twitter_to_kafka_service.runner.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import twitter_to_kafka_service.config.TwitterToKafkaServiceConfig;
import twitter_to_kafka_service.model.TweetConstants;
import twitter_to_kafka_service.model.TweetDTO;
import twitter_to_kafka_service.runner.StreamRunner;

import java.io.*;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "true")
public class MockKafkaStreamRunnerImpl implements StreamRunner {

    private final TwitterToKafkaServiceConfig twitterToKafkaServiceConfig;
    private final List<TweetDTO> tweetDTOList;

    @Override
    public void start() throws TwitterException, FileNotFoundException {
        prepareTweetListFromCsv();
    }

    private List<TweetDTO> prepareTweetListFromCsv() throws FileNotFoundException {
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
