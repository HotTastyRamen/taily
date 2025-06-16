package dev.HTR.services.utilServices;

import dev.HTR.DTOs.NotificationEvent;
import dev.HTR.DTOs.SignatureConfirmationRequestEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSmsRequest(NotificationEvent event) {
        kafkaTemplate.send("test-topic", event);
    }

    public void sendSignatureConfirmationRequest(SignatureConfirmationRequestEvent event) {
        kafkaTemplate.send("signature-confirmation-request", event);
    }


}