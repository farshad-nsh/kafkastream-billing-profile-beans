package domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author farshad noravesh
 * @version 1.0.0
 */

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamsConfiguration.class);

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topic.streamRawDataTopic}")
    private String rawDataTopic;

    @Value("${kafka.topic.streamProcessedDataTopic}")
    private String processedDataTopic;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kafkaStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "billingApp");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        //props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.serdeFrom(Billing.class));
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        return new StreamsConfig(props);
    }

    @Profile("dev")
    @Bean
    public KStream<String, String> billingCal(StreamsBuilder kStreamBuilder) {
        System.out.println("---------Tourchi billing processor--------");
        KStream<String, String> s1 = kStreamBuilder.stream(rawDataTopic);
        KStream<String, Billing> s2=s1.mapValues(messageValue -> {
            return new Billing().builder().findBillingValue(messageValue);
        });
        KStream<String, String> s3=s2.mapValues(m-> String.valueOf(m.getBillingValue()));
        s3.to(processedDataTopic);
        LOGGER.info("Stream4 started...");
        return s3;
    }

}