package domain;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author farshad noravesh
 * @version 1.0.0
 */

@Configuration
@EnableKafka
@EnableKafkaStreams
public class DevBeans {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamsConfiguration.class);

    @Value("${kafka.topic.streamRawDataTopic}")
    private String rawDataTopic;

    @Value("${kafka.topic.streamProcessedDataTopic}")
    private String processedDataTopic;

    @Profile("test")
    @Bean
    public KStream<String, String> kafkaStream(StreamsBuilder kStreamBuilder) {
        System.out.println("---------Here2--------");
        KStream<String, String> stream = kStreamBuilder.stream(rawDataTopic);

        stream.mapValues(messageValue -> {

            LOGGER.info(" 111Stream:SimpleKafkaStream processing payloads='{}'", messageValue);
            return new StringBuilder(messageValue).reverse().toString();
        }).to(processedDataTopic);
        LOGGER.info("Stream2 started ...");
        return stream;
    }


    @Profile("test")
    @Bean
    public KStream<String, String> billingUpperCase(StreamsBuilder kStreamBuilder) {
        System.out.println("---------Here3--------");
        KStream<String, String> stream = kStreamBuilder.stream(rawDataTopic);

        stream.mapValues(messageValue -> {

            //LOGGER.info(" Stream4:SimpleKafkaStream processing payloads='{}'", messageValue);
            return new StringBuilder(messageValue).toString().toUpperCase();

        }).to(processedDataTopic);
        LOGGER.info("Stream3 started...");
        return stream;
    }



}
