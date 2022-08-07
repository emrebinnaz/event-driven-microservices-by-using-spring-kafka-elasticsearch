package twitter_to_kafka_service.model;

import java.util.HashMap;
import java.util.Map;

public class TweetLabelFactory {

    public static Map<String, TweetLabel> tweetLabelMap = new HashMap<>();

    public static TweetLabel getTweetLabel(String label) {
        TweetLabel tweetLabel = tweetLabelMap.get(label);
        if(tweetLabel == null) {
            tweetLabel = TweetLabel.valueOf(label.toUpperCase());
            tweetLabelMap.put(tweetLabel.getTextForm(), tweetLabel);
        }
        return tweetLabel;
    }
}
