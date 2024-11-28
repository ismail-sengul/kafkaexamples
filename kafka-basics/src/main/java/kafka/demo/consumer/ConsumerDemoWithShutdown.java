package kafka.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ConsumerDemoWithShutdown {

    private static Logger logger = LoggerFactory.getLogger(ConsumerDemoWithShutdown.class.getSimpleName());

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

        //Auto commit interval settings
//        consumerProperties.setProperty("enable.auto.commit", "true");
//        consumerProperties.setProperty("auto.commit.interval.ms", "4000"); //after 4s, consumer will do auto commit

        //Create Consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(consumerProperties);

        //get a referance to the main thread
        final Thread mainThread = Thread.currentThread();

        //adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                logger.info("Detected a shutdown, let's exit by calling consumer.wakeup()...");
                consumer.wakeup();

                // join the main thread to allow the execution of the code in the main thread
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        try {
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
        } catch (WakeupException e) {
            logger.info("Consumer is starting to shut down.");
        }catch (Exception e) {
            logger.error("Unexpected exception in the consumer ", e);
        }finally {
            consumer.close(); //close the consumer, this will also commit offsets
            logger.info("The consumer is now gracefully shut down");
        }

    }
}
