package dev.HTR.services;

import dev.HTR.DTOs.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(NotificationEvent event) {
        kafkaTemplate.send("test-topic", event);
    }
}
