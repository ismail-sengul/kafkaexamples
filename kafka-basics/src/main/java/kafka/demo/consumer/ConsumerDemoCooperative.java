package kafka.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ConsumerDemoCooperative {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerDemoCooperative.class.getSimpleName());

    public static void main(String[] args) {

        logger.info("This is consumer demo main class. This class shows a Consumer processes in Kafka.");

        String groupId = "kafka-group";
        String topic= "kafkaexample";

        //Create Consumer Properties

        //Properties tell how to connect kafka and how to serialize key and value.
        Properties consumerProperties = new Properties();

        consumerProperties.setProperty("bootstrap.servers", "localhost:9092");
        consumerProperties.setProperty("key.deserializer", StringDeserializer.class.getName());
        consumerProperties.setProperty("value.deserializer", StringDeserializer.class.getName());
        consumerProperties.setProperty("group.id", groupId);
        consumerProperties.setProperty("auto.offset.reset", "earliest");
        // we can set partition assignment strategy
        consumerProperties.setProperty("partition.assignment.strategy", CooperativeStickyAssignor.class.getName());
        // consumerProperties.setProperty("group.instance.id","kafkaexample-2"); //strategy for static assignments

        //Create Consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(consumerProperties);

        //Subscribe a topic
        consumer.subscribe(List.of(topic));

        //Pool data
        while (true) {
            logger.info("Polling");
            ConsumerRecords<String,String> consumerRecords =
                    consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String,String> record : consumerRecords){
                logger.info("Key: " + record.key() + ", Value: " + record.value());
                logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
            }
        }

    }
}
