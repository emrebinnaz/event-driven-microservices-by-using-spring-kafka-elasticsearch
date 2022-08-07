package twitter_to_kafka_service.runner;

import twitter4j.TwitterException;

import java.io.FileNotFoundException;

public interface StreamRunner {

    void start() throws TwitterException, FileNotFoundException;
}
