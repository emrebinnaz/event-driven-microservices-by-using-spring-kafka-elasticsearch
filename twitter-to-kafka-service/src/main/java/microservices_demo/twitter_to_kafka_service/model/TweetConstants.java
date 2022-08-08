package microservices_demo.twitter_to_kafka_service.model;

public final class TweetConstants {
    public static final String DATASET_PATH = "/home/emre/Desktop/Spring/microservices-demo/twitter-to-kafka-service/src/main/resources/datasets/Tweets.csv";
    private TweetConstants() throws IllegalAccessException {
        throw new IllegalAccessException("It is private class");
    }
}
