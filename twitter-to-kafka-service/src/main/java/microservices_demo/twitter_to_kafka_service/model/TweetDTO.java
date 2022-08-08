package microservices_demo.twitter_to_kafka_service.model;


import lombok.Getter;

@Getter
public final class TweetDTO {

    private final String tweetId;
    private final String text;
    private final TweetLabel tweetLabel;

    private TweetDTO(String tweetId, String text, TweetLabel tweetLabel) {
        this.tweetId = tweetId;
        this.text = text;
        this.tweetLabel = tweetLabel;
    }

    public static TweetDTO getInstance(String [] values) {
        final String tweetId = values[0];
        final String text = values[1];
        final TweetLabel tweetLabel = TweetLabelFactory.getTweetLabel(values[2]);

        return new TweetDTO(tweetId, text, tweetLabel);
    }
}
