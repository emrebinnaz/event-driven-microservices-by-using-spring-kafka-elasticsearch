package microservices_demo.twitter_to_kafka_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices_demo.twitter_to_kafka_service.init.StreamInitializer;
import microservices_demo.twitter_to_kafka_service.listener.TweetSender;
import microservices_demo.twitter_to_kafka_service.listener.TwitterKafkaStatusListener;
import microservices_demo.twitter_to_kafka_service.model.TweetDTO;
import microservices_demo.twitter_to_kafka_service.runner.MockStreamRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import microservices_demo.twitter_to_kafka_service.runner.StreamRunner;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@ComponentScan(basePackages = "microservices_demo")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private final MockStreamRunner mockStreamRunner;
    private final StreamInitializer streamInitializer;
    private final TweetSender tweetSender;

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        streamInitializer.init();
        final List<TweetDTO> tweets = mockStreamRunner.getTweets();
        tweetSender.sendTweetsToKafkaProducer(tweets);

    }
}
