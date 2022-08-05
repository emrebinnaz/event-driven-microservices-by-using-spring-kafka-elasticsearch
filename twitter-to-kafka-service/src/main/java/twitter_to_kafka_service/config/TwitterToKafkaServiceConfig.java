package twitter_to_kafka_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Data
@ConfigurationProperties(prefix = "twitter-to-kafka-service")
public class TwitterToKafkaServiceConfig {

    private List<String> twitterKeywords; // it must match with "twitter-keywords"
    private String welcomeMessage;
}
