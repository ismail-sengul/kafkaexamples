package com.kafkawithspringboot.consumer.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer/api/v1")
public class ConsumerController {

    @KafkaListener(topics = "${spring.kafka.producer.topic}",groupId = "${spring.kafka.consumer.group-id}")
    public void listenMessages(String message) {
        System.out.println("Message received: " + message);
    }
}
