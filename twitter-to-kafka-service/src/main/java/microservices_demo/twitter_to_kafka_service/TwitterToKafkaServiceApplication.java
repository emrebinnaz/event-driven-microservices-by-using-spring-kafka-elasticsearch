package microservices_demo.twitter_to_kafka_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import microservices_demo.twitter_to_kafka_service.runner.StreamRunner;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@ComponentScan(basePackages = "microservices_demo")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private final StreamRunner streamRunner;

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        streamRunner.start();
    }
}
