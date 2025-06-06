package dev.HTR.services;
import dev.HTR.DTOs.NotificationEvent;
import dev.HTR.DTOs.VerificationCode;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaConsumerService {

    private final SmsService smsService;

    private CacheService cacheService;

    @KafkaListener(topics = "test-topic", groupId = "NotificationGroup")
    public void listen(NotificationEvent message) {

        System.out.println("Получено сообщение: " + message);

        System.out.println(message.getRecipient() + message.getType() + message.getUserId());

        VerificationCode code = new VerificationCode(cacheService.createVerCode(message.getUserId()));

        //smsService.sendSms(message.getRecipient(), code.getCode().toString());
    }
}
