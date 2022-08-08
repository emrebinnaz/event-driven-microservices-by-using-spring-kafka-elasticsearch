package microservices_demo.twitter_to_kafka_service.model;

import lombok.Getter;

@Getter
public enum TweetLabel {

    HATEFUL("HATEFUL"),
    NORMAL("NORMAL");

    private final String textForm;

    TweetLabel(String textForm) {
        this.textForm = textForm;
    }
}
