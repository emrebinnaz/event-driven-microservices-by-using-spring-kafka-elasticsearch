package twitter_to_kafka_service.runner.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter_to_kafka_service.config.TwitterToKafkaServiceConfig;
import twitter_to_kafka_service.listener.TwitterKafkaStatusListener;
import twitter_to_kafka_service.runner.StreamRunner;

import javax.annotation.PreDestroy;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "false", matchIfMissing = true)
//matchIfMissing provides to inject this class if there is not enable-mock-tweets property.
public class TwitterKafkaStreamRunnerImpl implements StreamRunner {

    private final TwitterToKafkaServiceConfig twitterToKafkaServiceConfig;
    private final TwitterKafkaStatusListener twitterKafkaStatusListener;
    private TwitterStream twitterStream;

    @Override
    public void start() throws TwitterException {
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(twitterKafkaStatusListener);
        addFilter();

    }

    private void addFilter() {
        final String[] keywords = twitterToKafkaServiceConfig.getTwitterKeywords().toArray(new String[0]);
        final FilterQuery filterQuery = new FilterQuery(keywords);
        twitterStream.filter(filterQuery);
        log.info("Started filtering twitter stream for keywords {}", Arrays.toString(keywords));
    }
    @PreDestroy
    public void shutdown() {
        if(twitterStream != null){
            log.info("Twitter stream is closing...");
            twitterStream.shutdown();
        }
    }
}
