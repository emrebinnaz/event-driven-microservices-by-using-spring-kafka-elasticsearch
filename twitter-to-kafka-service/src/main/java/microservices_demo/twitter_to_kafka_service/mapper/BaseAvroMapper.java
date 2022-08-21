package microservices_demo.twitter_to_kafka_service.mapper;

import java.util.List;

public interface BaseAvroMapper<D, S> {
    D mapFrom(S source);
    List<D> mapListFrom(List<S> source);

}
