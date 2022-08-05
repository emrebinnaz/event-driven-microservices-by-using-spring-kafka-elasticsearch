package twitter_to_kafka_service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import twitter_to_kafka_service.config.TwitterToKafkaServiceConfig;

import java.util.Arrays;

@SpringBootApplication
@RequiredArgsConstructor
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private final TwitterToKafkaServiceConfig twitterToKafkaServiceConfig;
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("App starts");
        LOGGER.info(Arrays.toString(twitterToKafkaServiceConfig.getTwitterKeywords().toArray()));
        LOGGER.info(twitterToKafkaServiceConfig.getWelcomeMessage());
    }
}
