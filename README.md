# kafkastream-billing-profile-beans
a light tutorial for using kafka stream API of billing class and using @profile to select active beans for the streams that we care at development and test time

## Kafka Streams for handling microservice events reactively
The best way to learn kafka stream is reading the book:"Kafka streams in action" By William P. Bejeck Jr.
The most advanced chapter is chapter6 about processor API and also all materials for stateful streams by Stores(page 231).

<code>
         
         KStream<String, String> s1 = kStreamBuilder.stream(rawDataTopic);
         KStream<String, Billing> s2=s1.mapValues(messageValue -> {         
             return new Billing().builder().findBillingValue(messageValue);         
         });        
        KStream<String, String> s3=s2.mapValues(m-> String.valueOf(m.getBillingValue()));
        s3.to(processedDataTopic);
<code/>

<p> 
 
 First I used builder design pattern for Billing class. Then i used 
some streams and one processor to consume the stream.
</p>

## @Profile("dev")

profiles help us choose which group of beans to execute.
in application.properties you can choose:
spring.profiles.active=dev




