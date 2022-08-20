package microservices_demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialRandomBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@RequiredArgsConstructor
public class RetryConfig {

    private final RetryConfigData retryConfigData;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialRandomBackOffPolicy exponentialRandomBackOffPolicy = new ExponentialRandomBackOffPolicy();
        // back off policy increases wait time for each retry attempt.
        exponentialRandomBackOffPolicy.setInitialInterval(retryConfigData.getInitialIntervalMs());
        exponentialRandomBackOffPolicy.setMaxInterval(retryConfigData.getMaxIntervalMs());
        exponentialRandomBackOffPolicy.setMultiplier(retryConfigData.getMultiplier());

        retryTemplate.setBackOffPolicy(exponentialRandomBackOffPolicy);

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(retryConfigData.getMaxAttempts());
        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        return retryTemplate;
    }

}
