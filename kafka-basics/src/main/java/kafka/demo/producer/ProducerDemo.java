package kafka.demo.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {

    private static final Logger logger = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {

        logger.info("This is producer demo main class. This class shows a Producer processes in Kafka.");

        //Create Producer Properties

        //Properties tell how to connect kafka and how to serialize key and value.
        Properties producerProperties = new Properties();
        producerProperties.setProperty("bootstrap.servers", "localhost:9092");
        producerProperties.setProperty("key.serializer", StringSerializer.class.getName());
        producerProperties.setProperty("value.serializer", StringSerializer.class.getName());


        //Other properties for producer without localhost
//        producerProperties.setProperty("security.protocol", "SASL_PLAINTEXT");
//        producerProperties.setProperty("sasl.mechanism", "PLAIN");
//        producerProperties.setProperty("sasl.jaas.config", "org.apache.kafka.common.serialization.StringSerializer");

        //Create Producer
        KafkaProducer<String,String> producer = new KafkaProducer<>(producerProperties);

        //Create a Producer Record
        ProducerRecord<String,String> record = new ProducerRecord<>("kafkaexample","hello_message","hello kafka");

        //Send Data
        producer.send(record);

        //Flush and Close the Producer

        // tell the producer to send all data and block until done  --  sync operation
        producer.flush();

        producer.close();

    }
}
