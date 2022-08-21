package microservices_demo.twitter_to_kafka_service.runner;

import microservices_demo.twitter_to_kafka_service.model.TweetDTO;
import twitter4j.TwitterException;

import java.io.FileNotFoundException;
import java.util.List;

public interface MockStreamRunner {

    List<TweetDTO> getTweets() throws TwitterException, FileNotFoundException;
}
