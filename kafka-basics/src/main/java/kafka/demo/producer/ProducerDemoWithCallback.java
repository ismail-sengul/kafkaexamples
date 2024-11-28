package kafka.demo.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {

    private static final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class.getSimpleName());

    public static void main(String[] args) {

        logger.info("This is producer demo with callback main class. This class shows a Producer processes in Kafka and send data with callback function.");

        //Create Producer Properties

        //Properties tell how to connect kafka and how to serialize key and value.
        Properties producerProperties = new Properties();
        producerProperties.setProperty("bootstrap.servers", "localhost:9092");
        producerProperties.setProperty("key.serializer", StringSerializer.class.getName());
        producerProperties.setProperty("value.serializer", StringSerializer.class.getName());
        producerProperties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());


        //Other properties for producer without localhost
//        producerProperties.setProperty("security.protocol", "SASL_PLAINTEXT");
//        producerProperties.setProperty("sasl.mechanism", "PLAIN");
//        producerProperties.setProperty("sasl.jaas.config", "org.apache.kafka.common.serialization.StringSerializer");

        //Create Producer
        KafkaProducer<String,String> producer = new KafkaProducer<>(producerProperties);

        //Send Data
        // sendData(producer,record);

        //Send Data To Topic Ten Times
        for (int i = 0; i < 10; i++) {
            String topic = "kafkaexample";
            String key = "key_id_"+i;
            String value = "value_"+i;

            ProducerRecord<String,String> record = new ProducerRecord<>(topic, key, value);

            sendData(producer,record);
        }


        //Flush and Close the Producer

        // tell the producer to send all data and block until done  --  sync operation
        producer.flush();

        producer.close();

    }

    private static void sendData(KafkaProducer producer, ProducerRecord<String, String> record) {
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                //executes every time a record successfully sent or an exception is thrown
                if (e != null) {
                    //the record not successfully send and an exception is thrown
                    logger.error("Error Message:" + e.getMessage());
                    throw new RuntimeException("Message is not sended.");
                }else {
                    logger.info("Received new metadata \n"+
                            "Topic: "+ recordMetadata.topic() +" \n"+
                            "Partition: "+ recordMetadata.partition() +" \n"+
                            "Offset: "+ recordMetadata.offset() +" \n"+
                            "Timestamp: "+ recordMetadata.timestamp() +" \n");

                }
            }
        });
    }
}
